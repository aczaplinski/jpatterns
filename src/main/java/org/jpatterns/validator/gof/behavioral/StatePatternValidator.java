package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.StatePattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class StatePatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public StatePatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(StatePattern.Context.class)) {
            validatorUtils.validateContainsFieldOfTypeAnnotatedWithAnyOf(annotatedElement,
                    StatePattern.Context.class,
                    StatePattern.State.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(StatePattern.State.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    StatePattern.State.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(StatePattern.ConcreteState.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    StatePattern.ConcreteState.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    StatePattern.ConcreteState.class,
                    StatePattern.ConcreteState.class,
                    StatePattern.State.class);
        }
    }
}
