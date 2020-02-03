package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.BuilderPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class BuilderPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public BuilderPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.Builder.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    BuilderPattern.Builder.class);
            validateContainsMethodReturningProduct(annotatedElement,
                    BuilderPattern.Builder.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.ConcreteBuilder.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    BuilderPattern.ConcreteBuilder.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    BuilderPattern.ConcreteBuilder.class,
                    BuilderPattern.ConcreteBuilder.class,
                    BuilderPattern.Builder.class);
            validateContainsMethodReturningProduct(annotatedElement,
                    BuilderPattern.ConcreteBuilder.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.Director.class)) {
            validateContainsMethodReturningProduct(annotatedElement,
                    BuilderPattern.Director.class);
        }
    }

    private void validateContainsMethodReturningProduct(Element annotatedElement,
                                                        Class<? extends Annotation> annotation) {
        if(annotatedElement.getEnclosedElements()
                .stream()
                .noneMatch(this::isMethodReturningProduct)) {
            validatorUtils.printMessage(
                    annotation.getSimpleName() + " %1$s contain a method returning Product.",
                    annotatedElement,
                    annotation);
        }
    }

    private boolean isMethodReturningProduct(Element element) {
        return element.getKind() == ElementKind.METHOD
               && Optional.ofNullable(validatorUtils.getReturnedElement((ExecutableElement) element))
                    .map(returnedElement -> returnedElement.getAnnotation(BuilderPattern.Product.class))
                    .isPresent();
    }

}
