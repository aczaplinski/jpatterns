package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.InterpreterPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class InterpreterPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public InterpreterPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(InterpreterPattern.Context.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    InterpreterPattern.Context.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(InterpreterPattern.AbstractExpression.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    InterpreterPattern.AbstractExpression.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(InterpreterPattern.TerminalExpression.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    InterpreterPattern.TerminalExpression.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    InterpreterPattern.TerminalExpression.class,
                    InterpreterPattern.TerminalExpression.class,
                    InterpreterPattern.AbstractExpression.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(InterpreterPattern.NonterminalExpression.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    InterpreterPattern.NonterminalExpression.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    InterpreterPattern.NonterminalExpression.class,
                    InterpreterPattern.NonterminalExpression.class,
                    InterpreterPattern.AbstractExpression.class);
        }
    }
}
