package org.jpatterns.validator;

import org.jpatterns.gof.creational.AbstractFactoryPattern;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
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
        System.out.println("PROCESSING " + annotations.size() + "!");
        System.out.println(annotations);
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractFactory.class)) {
            validateFactoryContainsFactoryMethod(annotatedElement);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractProduct.class)) {
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteFactory.class)) {
            validateFactoryContainsFactoryMethod(annotatedElement);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteProduct.class)) {
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.FactoryMethod.class)) {
            validateFactoryMethodIsInsideFactory(annotatedElement);
        }
        return false;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
                                                         ExecutableElement member, String userText) {
        return Collections.emptyList();
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
