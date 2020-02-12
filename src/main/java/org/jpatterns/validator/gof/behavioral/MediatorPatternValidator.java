package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.MediatorPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class MediatorPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public MediatorPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(MediatorPattern.Mediator.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    MediatorPattern.Mediator.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(MediatorPattern.ConcreteMediator.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    MediatorPattern.ConcreteMediator.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    MediatorPattern.ConcreteMediator.class,
                    MediatorPattern.ConcreteMediator.class,
                    MediatorPattern.Mediator.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(MediatorPattern.Colleague.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    MediatorPattern.Colleague.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(MediatorPattern.ConcreteColleague.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    MediatorPattern.ConcreteColleague.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    MediatorPattern.ConcreteColleague.class,
                    MediatorPattern.ConcreteColleague.class,
                    MediatorPattern.Colleague.class);
            validatorUtils.validateContainsFieldOfTypeAnnotatedWith(annotatedElement,
                    MediatorPattern.ConcreteColleague.class,
                    MediatorPattern.Mediator.class);
        }
    }
}
