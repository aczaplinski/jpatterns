package org.jpatterns.validator;

import org.jpatterns.gof.creational.AbstractFactoryPattern;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AbstractFactoryPatternValidator implements Processor {
    private Elements elements;
    private Messager messager;
    private Types types;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        types = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of("*");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_13;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractFactory.class)) {
            validateIsAbstractClassOrInterface(annotatedElement, AbstractFactoryPattern.AbstractFactory.class);
            validateFactoryContainsFactoryMethod(annotatedElement);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractProduct.class)) {
            validateIsAbstractClassOrInterface(annotatedElement, AbstractFactoryPattern.AbstractProduct.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteFactory.class)) {
            validateIsConcreteClass(annotatedElement, AbstractFactoryPattern.ConcreteFactory.class);
            validateFactoryContainsFactoryMethod(annotatedElement);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteProduct.class)) {
            validateIsConcreteClass(annotatedElement, AbstractFactoryPattern.ConcreteProduct.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.FactoryMethod.class)) {
            validateElementModifiersDoNotContain(annotatedElement, AbstractFactoryPattern.FactoryMethod.class,
                    Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC);
            validateFactoryMethodIsInsideFactory(annotatedElement);
            validateFactoryMethodReturnsProduct(annotatedElement);
        }
        return false;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
                                                         ExecutableElement member, String userText) {
        return Collections.emptyList();
    }

    private void validateIsConcreteClass(Element annotatedType, Class annotation) {
        if(annotatedType.getKind() == ElementKind.CLASS) {
            validateElementModifiersDoNotContain(annotatedType, annotation, Modifier.ABSTRACT);
        } else {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    annotation.getSimpleName() + " must not be an interface.",
                    annotatedType,
                    getElementAnnotationMirror(annotatedType, annotation));
        }
    }

    private void validateIsAbstractClassOrInterface(Element annotatedType, Class annotation) {
        if(annotatedType.getKind() == ElementKind.CLASS
                && !annotatedType.getModifiers().contains(Modifier.ABSTRACT)) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    annotation.getSimpleName() + " must be an abstract class or an interface.",
                    annotatedType,
                    getElementAnnotationMirror(annotatedType, annotation));
        }
    }

    private void validateElementModifiersDoNotContain(Element annotatedElement, Class annotation,
                                                      Modifier ... modifiers) {
        Arrays.stream(modifiers)
                .filter(modifier -> annotatedElement.getModifiers().contains(modifier))
                .forEach(modifier -> messager.printMessage(Diagnostic.Kind.ERROR,
                    annotation.getSimpleName() + " must not be " + modifier,
                    annotatedElement,
                    getElementAnnotationMirror(annotatedElement, annotation)));
    }

    private void validateFactoryMethodIsInsideFactory(Element annotatedFactoryMethod) {
        if(annotatedFactoryMethod.getEnclosingElement().getAnnotation(AbstractFactoryPattern.AbstractFactory.class)
                == null
            && annotatedFactoryMethod.getEnclosingElement().getAnnotation(AbstractFactoryPattern.ConcreteFactory.class)
                == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Factory Method must reside in a class or interface" +
                    " annotated with either @AbstractFactory or @ConcreteFactory", annotatedFactoryMethod,
                    getElementAnnotationMirror(annotatedFactoryMethod, AbstractFactoryPattern.FactoryMethod.class));
        }
    }

    private void validateFactoryContainsFactoryMethod(Element annotatedFactory) {
        if(annotatedFactory.getEnclosedElements()
                .stream()
                .noneMatch(potentialFactoryMethod ->
                        potentialFactoryMethod.getAnnotation(AbstractFactoryPattern.FactoryMethod.class) != null)) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Factory must contain a method annotated with @FactoryMethod",
                    annotatedFactory,
                    getElementAnnotationMirror(annotatedFactory,
                            AbstractFactoryPattern.AbstractFactory.class,
                            AbstractFactoryPattern.ConcreteFactory.class));
        }
    }

    private void validateFactoryMethodReturnsProduct(Element annotatedFactoryMethod) {
        Element returnedElement = types.asElement(((ExecutableElement) annotatedFactoryMethod).getReturnType());
        if(returnedElement.getAnnotation(AbstractFactoryPattern.AbstractProduct.class) == null
            && returnedElement.getAnnotation(AbstractFactoryPattern.ConcreteProduct.class) == null) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Factory Method must return a value of type annotated" +
                            " with @AbstractProduct or @ConcreteProduct",
                    annotatedFactoryMethod,
                    getElementAnnotationMirror(annotatedFactoryMethod,
                            AbstractFactoryPattern.FactoryMethod.class)
                    );
        }
    }

    private AnnotationMirror getElementAnnotationMirror(Element annotatedElement, Class ... annotations) {
        List<String> annotationQualifiedNames = Arrays.stream(annotations)
                .map(annotation -> annotation.getName().replace('$', '.'))
                .collect(Collectors.toList());
        return elements.getAllAnnotationMirrors(annotatedElement)
                .stream()
                .filter(annotationMirror -> annotationQualifiedNames.contains(
                        annotationMirror
                            .getAnnotationType()
                            .toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Supplied Element is not annotated with any of the given annotations"));
    }
}
