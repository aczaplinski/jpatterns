package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.StrategyPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class StrategyPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public StrategyPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(StrategyPattern.Context.class)) {
            validatorUtils.validateTypeContainsElementAnnotatedWithAnyOf(annotatedElement,
                    StrategyPattern.Context.class,
                    StrategyPattern.StrategyField.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(StrategyPattern.StrategyField.class)) {
            validatorUtils.validateEnclosingTypeIsAnnotatedWithAnyOf(annotatedElement,
                    StrategyPattern.StrategyField.class,
                    StrategyPattern.Context.class);
            validatorUtils.validateFieldTypeIsAnnotatedWith(annotatedElement,
                    StrategyPattern.StrategyField.class,
                    StrategyPattern.Strategy.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(StrategyPattern.Strategy.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    StrategyPattern.Strategy.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(StrategyPattern.ConcreteStrategy.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    StrategyPattern.ConcreteStrategy.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    StrategyPattern.ConcreteStrategy.class,
                    StrategyPattern.ConcreteStrategy.class,
                    StrategyPattern.Strategy.class);
        }
    }
}
