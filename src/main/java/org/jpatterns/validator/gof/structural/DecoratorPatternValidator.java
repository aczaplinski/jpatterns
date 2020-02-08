package org.jpatterns.validator.gof.structural;

import org.jpatterns.gof.structural.DecoratorPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class DecoratorPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public DecoratorPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(DecoratorPattern.Component.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    DecoratorPattern.Component.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(DecoratorPattern.ConcreteComponent.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    DecoratorPattern.ConcreteComponent.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    DecoratorPattern.ConcreteComponent.class,
                    DecoratorPattern.ConcreteComponent.class,
                    DecoratorPattern.Component.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(DecoratorPattern.Decorator.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    DecoratorPattern.Decorator.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    DecoratorPattern.Decorator.class,
                    DecoratorPattern.Decorator.class,
                    DecoratorPattern.Component.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(DecoratorPattern.ConcreteDecorator.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    DecoratorPattern.ConcreteDecorator.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    DecoratorPattern.ConcreteDecorator.class,
                    DecoratorPattern.ConcreteDecorator.class,
                    DecoratorPattern.Decorator.class);
        }
    }
}
