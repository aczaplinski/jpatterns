package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.PrototypePattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class PrototypePatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public PrototypePatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(PrototypePattern.ConcretePrototype.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    PrototypePattern.ConcretePrototype.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    PrototypePattern.ConcretePrototype.class,
                    PrototypePattern.ConcretePrototype.class,
                    PrototypePattern.Prototype.class);
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    PrototypePattern.ConcretePrototype.class,
                    PrototypePattern.ConcretePrototype.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(PrototypePattern.Prototype.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    PrototypePattern.Prototype.class);
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    PrototypePattern.Prototype.class,
                    PrototypePattern.Prototype.class);
        }
    }

}
