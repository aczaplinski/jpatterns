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
import java.util.Collections;
import java.util.Set;

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
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractProduct.class)) {
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteFactory.class)) {
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

    private void validateFactoryMethodIsInsideFactory(Element annotatedElement) {
        if(annotatedElement.getEnclosingElement().getAnnotation(AbstractFactoryPattern.AbstractFactory.class)
                == null
            && annotatedElement.getEnclosingElement().getAnnotation(AbstractFactoryPattern.ConcreteFactory.class)
                == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Factory Method must reside in a class or interface" +
                    " annotated with either @AbstractFactory or @ConcreteFactory", annotatedElement,
                    elements.getAllAnnotationMirrors(annotatedElement)
                            .stream()
                            .filter(annotationMirror -> annotationMirror
                                    .getAnnotationType()
                                    .toString()
                                    .equals(AbstractFactoryPattern.FactoryMethod.class
                                            .getName()
                                            .replace('$', '.')))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("This can never happen")));
        }
    }
}
