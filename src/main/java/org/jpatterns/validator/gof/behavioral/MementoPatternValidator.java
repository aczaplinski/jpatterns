package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.MementoPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class MementoPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public MementoPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(MementoPattern.Originator.class)) {
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    MementoPattern.Originator.class,
                    MementoPattern.Memento.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(MementoPattern.Memento.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    MementoPattern.Memento.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(MementoPattern.MementoImpl.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    MementoPattern.MementoImpl.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    MementoPattern.MementoImpl.class,
                    MementoPattern.MementoImpl.class,
                    MementoPattern.Memento.class);
        }
    }
}
