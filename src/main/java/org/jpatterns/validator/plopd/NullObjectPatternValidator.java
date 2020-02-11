package org.jpatterns.validator.plopd;

import org.jpatterns.plopd.NullObjectPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class NullObjectPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public NullObjectPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(NullObjectPattern.AbstractObject.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    NullObjectPattern.AbstractObject.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(NullObjectPattern.RealObject.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    NullObjectPattern.RealObject.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    NullObjectPattern.RealObject.class,
                    NullObjectPattern.RealObject.class,
                    NullObjectPattern.AbstractObject.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(NullObjectPattern.NullObject.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    NullObjectPattern.NullObject.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    NullObjectPattern.NullObject.class,
                    NullObjectPattern.AbstractObject.class);
        }
    }
}
