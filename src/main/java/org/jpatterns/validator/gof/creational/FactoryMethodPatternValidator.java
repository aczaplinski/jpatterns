package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.FactoryMethodPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

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
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    FactoryMethodPattern.ConcreteCreator.class,
                    FactoryMethodPattern.ConcreteProduct.class,
                    FactoryMethodPattern.Product.class);
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
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    FactoryMethodPattern.Creator.class,
                    FactoryMethodPattern.Product.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FactoryMethodPattern.Product.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    FactoryMethodPattern.Product.class);
        }
    }

}
