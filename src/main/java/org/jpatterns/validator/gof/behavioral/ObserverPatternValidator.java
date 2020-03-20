package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.ObserverPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class ObserverPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public ObserverPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ObserverPattern.Observer.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    ObserverPattern.Observer.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ObserverPattern.ConcreteObserver.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    ObserverPattern.ConcreteObserver.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    ObserverPattern.ConcreteObserver.class,
                    ObserverPattern.ConcreteObserver.class,
                    ObserverPattern.Observer.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ObserverPattern.Subject.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    ObserverPattern.Subject.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ObserverPattern.ConcreteSubject.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    ObserverPattern.ConcreteSubject.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    ObserverPattern.ConcreteSubject.class,
                    ObserverPattern.ConcreteSubject.class,
                    ObserverPattern.Subject.class);
            validatorUtils.validateContainsFieldOfTypeAnnotatedWithAnyOf(annotatedElement,
                    ObserverPattern.ConcreteSubject.class,
                    ObserverPattern.Observer.class,
                    ObserverPattern.ConcreteObserver.class);
        }
    }
}
