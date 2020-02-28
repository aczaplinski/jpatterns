package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.ChainOfResponsibilityPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class ChainOfResponsibilityPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public ChainOfResponsibilityPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ChainOfResponsibilityPattern.Handler.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    ChainOfResponsibilityPattern.Handler.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ChainOfResponsibilityPattern.ConcreteHandler.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    ChainOfResponsibilityPattern.ConcreteHandler.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    ChainOfResponsibilityPattern.ConcreteHandler.class,
                    ChainOfResponsibilityPattern.ConcreteHandler.class,
                    ChainOfResponsibilityPattern.Handler.class);
            validateIsDefaultHandlerOrContainsNextHandlerField(annotatedElement);
        }
    }

    private void validateIsDefaultHandlerOrContainsNextHandlerField(Element annotatedConcreteHandler) {
        if(!annotatedConcreteHandler.getAnnotation(ChainOfResponsibilityPattern.ConcreteHandler.class)
                .defaultHandler()) {
            if(!validatorUtils.containsFieldOfTypeAnnotatedWith(annotatedConcreteHandler,
                    ChainOfResponsibilityPattern.Handler.class)) {
                validatorUtils.printMessage(
                        "ConcreteHandler that is not a default Handler %1$s store Handler reference",
                        annotatedConcreteHandler,
                        ChainOfResponsibilityPattern.ConcreteHandler.class);
            }
        }
    }
}
