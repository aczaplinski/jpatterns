package org.jpatterns.validator.gof.structural;

import org.jpatterns.gof.structural.CompositePattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class CompositePatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public CompositePatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(CompositePattern.Component.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    CompositePattern.Component.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(CompositePattern.Leaf.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    CompositePattern.Leaf.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    CompositePattern.Leaf.class,
                    CompositePattern.Leaf.class,
                    CompositePattern.Component.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(CompositePattern.Composite.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    CompositePattern.Composite.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    CompositePattern.Composite.class,
                    CompositePattern.Composite.class,
                    CompositePattern.Component.class);
        }
    }
}
