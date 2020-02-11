package org.jpatterns.validator.gof.structural;

import org.jpatterns.gof.structural.FlyweightPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class FlyweightPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public FlyweightPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FlyweightPattern.FlyweightFactory.class)) {
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    FlyweightPattern.FlyweightFactory.class,
                    FlyweightPattern.Flyweight.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FlyweightPattern.Flyweight.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    FlyweightPattern.Flyweight.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FlyweightPattern.UnsharedConcreteFlyweight.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    FlyweightPattern.UnsharedConcreteFlyweight.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    FlyweightPattern.UnsharedConcreteFlyweight.class,
                    FlyweightPattern.UnsharedConcreteFlyweight.class,
                    FlyweightPattern.ConcreteFlyweight.class,
                    FlyweightPattern.Flyweight.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(FlyweightPattern.ConcreteFlyweight.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    FlyweightPattern.ConcreteFlyweight.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    FlyweightPattern.ConcreteFlyweight.class,
                    FlyweightPattern.ConcreteFlyweight.class,
                    FlyweightPattern.Flyweight.class);
        }
    }
}
