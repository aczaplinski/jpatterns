package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.FactoryMethodPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class FactoryMethodPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public FactoryMethodPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FactoryMethodPattern.ConcreteCreator.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    FactoryMethodPattern.ConcreteCreator.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    FactoryMethodPattern.ConcreteCreator.class,
                    FactoryMethodPattern.ConcreteCreator.class,
                    FactoryMethodPattern.Creator.class);
            validateContainsMethodReturningProduct(annotatedElement,
                    FactoryMethodPattern.ConcreteCreator.class,
                    true);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FactoryMethodPattern.ConcreteProduct.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    FactoryMethodPattern.ConcreteProduct.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    FactoryMethodPattern.ConcreteProduct.class,
                    FactoryMethodPattern.ConcreteProduct.class,
                    FactoryMethodPattern.Product.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FactoryMethodPattern.Creator.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    FactoryMethodPattern.Creator.class);
            validateContainsMethodReturningProduct(annotatedElement,
                    FactoryMethodPattern.Creator.class,
                    false);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FactoryMethodPattern.Product.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    FactoryMethodPattern.Product.class);
        }
    }

    private void validateContainsMethodReturningProduct(Element annotatedElement,
                                                        Class<? extends Annotation> annotation,
                                                        boolean mustBeConcrete) {
        if(annotatedElement.getEnclosedElements()
                .stream()
                .noneMatch(element -> isMethodReturningProduct(element, mustBeConcrete))) {
            validatorUtils.printMessage(
                    annotation.getSimpleName()
                            + " %1$s contain a method returning"
                            + (mustBeConcrete ? " Concrete" : "")
                            + " Product.",
                    annotatedElement,
                    annotation);
        }
    }

    private boolean isMethodReturningProduct(Element element, boolean mustBeConcrete) {
        return element.getKind() == ElementKind.METHOD
               && Optional.ofNullable(validatorUtils.getReturnedElement((ExecutableElement) element))
                    .map(returnedElement ->
                            returnedElement.getAnnotation(FactoryMethodPattern.ConcreteProduct.class) != null
                            || (!mustBeConcrete
                                    && returnedElement.getAnnotation(FactoryMethodPattern.Product.class) != null))
                    .orElse(false);
    }

}
