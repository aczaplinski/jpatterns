package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.CommandPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class CommandPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public CommandPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(CommandPattern.Command.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    CommandPattern.Command.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(CommandPattern.ConcreteCommand.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    CommandPattern.ConcreteCommand.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    CommandPattern.ConcreteCommand.class,
                    CommandPattern.ConcreteCommand.class,
                    CommandPattern.Command.class);
            validatorUtils.validateContainsFieldOfTypeAnnotatedWithAnyOf(annotatedElement,
                    CommandPattern.ConcreteCommand.class,
                    CommandPattern.Receiver.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(CommandPattern.Invoker.class)) {
            validatorUtils.validateContainsFieldOfTypeAnnotatedWithAnyOf(annotatedElement,
                    CommandPattern.Invoker.class,
                    CommandPattern.Command.class);
        }
    }
}
