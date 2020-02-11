package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.VisitorPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class VisitorPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public VisitorPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(VisitorPattern.Visitor.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    VisitorPattern.Visitor.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(VisitorPattern.ConcreteVisitor.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    VisitorPattern.ConcreteVisitor.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    VisitorPattern.ConcreteVisitor.class,
                    VisitorPattern.ConcreteVisitor.class,
                    VisitorPattern.Visitor.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(VisitorPattern.Element.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    VisitorPattern.Element.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(VisitorPattern.ConcreteElement.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    VisitorPattern.ConcreteElement.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    VisitorPattern.ConcreteElement.class,
                    VisitorPattern.ConcreteElement.class,
                    VisitorPattern.Element.class);
        }
    }
}
