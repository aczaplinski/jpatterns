package org.jpatterns.validator.gof.structural;

import org.jpatterns.gof.structural.BridgePattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class BridgePatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public BridgePatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BridgePattern.Abstraction.class)) {
            /* This is not a bug - Abstraction usually is a concrete class,
               and because it is an easy to make mistake to make it abstract,
               there is a check for that. If someone really needs an abstract
               Abstraction, they can set validationErrorLevel to NONE in this
               annotation.
             */
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    BridgePattern.Abstraction.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BridgePattern.RefinedAbstraction.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    BridgePattern.RefinedAbstraction.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    BridgePattern.RefinedAbstraction.class,
                    BridgePattern.RefinedAbstraction.class,
                    BridgePattern.Abstraction.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BridgePattern.Implementor.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    BridgePattern.Implementor.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BridgePattern.ConcreteImplementor.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    BridgePattern.ConcreteImplementor.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    BridgePattern.ConcreteImplementor.class,
                    BridgePattern.ConcreteImplementor.class,
                    BridgePattern.Implementor.class);
        }
    }
}
