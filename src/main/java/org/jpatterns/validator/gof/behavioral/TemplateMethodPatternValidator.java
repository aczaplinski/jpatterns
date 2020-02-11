package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.TemplateMethodPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class TemplateMethodPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public TemplateMethodPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(TemplateMethodPattern.AbstractClass.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    TemplateMethodPattern.AbstractClass.class);
            validatorUtils.validateTypeContainsElementAnnotatedWith(annotatedElement,
                    TemplateMethodPattern.AbstractClass.class,
                    TemplateMethodPattern.TemplateMethod.class);
            validatorUtils.validateTypeContainsElementAnnotatedWith(annotatedElement,
                    TemplateMethodPattern.AbstractClass.class,
                    TemplateMethodPattern.PrimitiveOperation.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(TemplateMethodPattern.ConcreteClass.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    TemplateMethodPattern.ConcreteClass.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    TemplateMethodPattern.ConcreteClass.class,
                    TemplateMethodPattern.ConcreteClass.class,
                    TemplateMethodPattern.AbstractClass.class);
            validatorUtils.validateTypeContainsElementAnnotatedWith(annotatedElement,
                    TemplateMethodPattern.ConcreteClass.class,
                    TemplateMethodPattern.PrimitiveOperation.class);
        }
    }
}
