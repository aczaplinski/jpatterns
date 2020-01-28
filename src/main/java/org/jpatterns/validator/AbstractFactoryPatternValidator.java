package org.jpatterns.validator;

import org.jpatterns.core.ValidationErrorLevel;
import org.jpatterns.gof.creational.AbstractFactoryPattern;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
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

    private void validateIsConcreteClass(Element annotatedType,
                                         Class<? extends Annotation> annotation) {
        if(annotatedType.getKind() == ElementKind.CLASS) {
            validateElementModifiersDoNotContain(annotatedType, annotation, Modifier.ABSTRACT);
        } else {
            printMessage(annotation.getSimpleName() + " %1$s not be an interface.",
                    annotatedType,
                    annotation);
        }
    }

    private void validateIsAbstractClassOrInterface(Element annotatedType,
                                                    Class<? extends Annotation> annotation) {
        if(annotatedType.getKind() == ElementKind.CLASS
                && !annotatedType.getModifiers().contains(Modifier.ABSTRACT)) {
            printMessage(annotation.getSimpleName() + " %1$s be an abstract class or an interface.",
                    annotatedType,
                    annotation);
        }
    }

    private void validateElementModifiersDoNotContain(Element annotatedElement,
                                                      Class<? extends Annotation> annotation,
                                                      Modifier ... modifiers) {
        Arrays.stream(modifiers)
                .filter(modifier -> annotatedElement.getModifiers().contains(modifier))
                .forEach(modifier -> printMessage(annotation.getSimpleName() + " %1$s not be " + modifier,
                    annotatedElement,
                    annotation));
    }

    private void validateFactoryMethodIsInsideFactory(Element annotatedFactoryMethod) {
        if(annotatedFactoryMethod.getEnclosingElement().getAnnotation(AbstractFactoryPattern.AbstractFactory.class)
                == null
            && annotatedFactoryMethod.getEnclosingElement().getAnnotation(AbstractFactoryPattern.ConcreteFactory.class)
                == null) {
            printMessage("Factory Method %1$s reside in a class or interface" +
                    " annotated with either @AbstractFactory or @ConcreteFactory",
                    annotatedFactoryMethod,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
    }

    private void validateFactoryContainsFactoryMethod(Element annotatedFactory) {
        if(annotatedFactory.getEnclosedElements()
                .stream()
                .noneMatch(potentialFactoryMethod ->
                        potentialFactoryMethod.getAnnotation(AbstractFactoryPattern.FactoryMethod.class) != null)) {
            printMessage("Factory %1$s contain a method annotated with @FactoryMethod",
                    annotatedFactory,
                    AbstractFactoryPattern.AbstractFactory.class,
                    AbstractFactoryPattern.ConcreteFactory.class);
        }
    }

    private void validateFactoryMethodReturnsProduct(Element annotatedFactoryMethod) {
        Element returnedElement = types.asElement(((ExecutableElement) annotatedFactoryMethod).getReturnType());
        if(returnedElement.getAnnotation(AbstractFactoryPattern.AbstractProduct.class) == null
            && returnedElement.getAnnotation(AbstractFactoryPattern.ConcreteProduct.class) == null) {
            printMessage("Factory Method %1$s return a value of type annotated" +
                            " with @AbstractProduct or @ConcreteProduct",
                    annotatedFactoryMethod,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
    }

    /** Generates a compiler message of appropriate level.
     * Replaces all occurrences of "%1$s" in the message
     * with a verb like must or should.
     * annotations must contain a class representing an annotation present on
     * annotatedElement. The generated message will point to it. */
    @SafeVarargs
    private void printMessage(String message,
                              Element annotatedElement,
                              Class<? extends Annotation> ... annotations) {
        Class<? extends Annotation> annotationPresent = Arrays.stream(annotations)
                .filter(annotation -> annotatedElement.getAnnotation(annotation) != null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Supplied Element is not annotated with any of the given annotations"));
        ValidationErrorLevel validationErrorLevel = getValidationErrorLevel(annotatedElement, annotationPresent);
        if(validationErrorLevel != ValidationErrorLevel.NONE) {
            messager.printMessage(validationErrorLevel.getDiagnosticKind(),
                    String.format(message, validationErrorLevel.getMessageVerb()),
                    annotatedElement,
                    getElementAnnotationMirror(annotatedElement,
                            annotationPresent));
        }
    }

    @SafeVarargs
    private AnnotationMirror getElementAnnotationMirror(Element annotatedElement,
                                                        Class<? extends Annotation> ... annotations) {
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

    private ValidationErrorLevel getValidationErrorLevel(Element annotatedElement,
                                                         Class<? extends  Annotation> annotation) {
        try {
            /* Annotations cannot extend nor implement anything, so there is no way to specify a common interface
                containing method validationErrorLevel(). That's why reflection is used here.
             */
            return (ValidationErrorLevel) annotation.getMethod("validationErrorLevel")
                    .invoke(annotatedElement.getAnnotation(annotation));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Could not retrieve validation error level", e);
        }
    }
}
