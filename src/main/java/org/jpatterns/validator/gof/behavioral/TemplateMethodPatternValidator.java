package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.TemplateMethodPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

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
            validatorUtils.validateTypeContainsElementAnnotatedWithAnyOf(annotatedElement,
                    TemplateMethodPattern.AbstractClass.class,
                    TemplateMethodPattern.TemplateMethod.class);
            validatorUtils.validateTypeContainsElementAnnotatedWithAnyOf(annotatedElement,
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
            validatorUtils.validateTypeContainsElementAnnotatedWithAnyOf(annotatedElement,
                    TemplateMethodPattern.ConcreteClass.class,
                    TemplateMethodPattern.PrimitiveOperation.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(TemplateMethodPattern.TemplateMethod.class)) {
            validatorUtils.validateElementModifiersContain(annotatedElement,
                    TemplateMethodPattern.TemplateMethod.class,
                    Modifier.FINAL);
            validatorUtils.validateElementModifiersDoNotContain(annotatedElement,
                    TemplateMethodPattern.TemplateMethod.class,
                    Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC);
            validatorUtils.validateEnclosingTypeIsAnnotatedWithAnyOf(annotatedElement,
                    TemplateMethodPattern.TemplateMethod.class,
                    TemplateMethodPattern.AbstractClass.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(TemplateMethodPattern.PrimitiveOperation.class)) {
            /* Not disallowing Modifier.DEFAULT - it is necessary when creating a method that can
             * be overridden only by subclasses from the same package */
            validatorUtils.validateElementModifiersDoNotContain(annotatedElement,
                    TemplateMethodPattern.PrimitiveOperation.class,
                    Modifier.PRIVATE, Modifier.PUBLIC, Modifier.STATIC);
            validatorUtils.validateEnclosingTypeIsAnnotatedWithAnyOf(annotatedElement,
                    TemplateMethodPattern.PrimitiveOperation.class,
                    TemplateMethodPattern.AbstractClass.class,
                    TemplateMethodPattern.ConcreteClass.class);
            validateClassIsAbstractOrPrimitiveOperationMethodHasOverride(annotatedElement);
        }
    }

    private void validateClassIsAbstractOrPrimitiveOperationMethodHasOverride(
            Element annotatedPrimitiveOperationMethod) {
        if(annotatedPrimitiveOperationMethod.getEnclosingElement()
                .getAnnotation(TemplateMethodPattern.ConcreteClass.class)
                != null
                && annotatedPrimitiveOperationMethod.getAnnotation(Override.class) == null) {
            validatorUtils.printMessage("Primitive Operation in Concrete Class %1$s override" +
                            " a Primitive Operation from its superclass, so it %1$s also be annotated with @Override",
                    annotatedPrimitiveOperationMethod,
                    TemplateMethodPattern.PrimitiveOperation.class);
        }
    }
}
