package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.BuilderPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class BuilderPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public BuilderPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.Builder.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    BuilderPattern.Builder.class);
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    BuilderPattern.Builder.class,
                    BuilderPattern.Product.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.ConcreteBuilder.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    BuilderPattern.ConcreteBuilder.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    BuilderPattern.ConcreteBuilder.class,
                    BuilderPattern.ConcreteBuilder.class,
                    BuilderPattern.Builder.class);
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    BuilderPattern.ConcreteBuilder.class,
                    BuilderPattern.Product.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.Director.class)) {
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    BuilderPattern.Director.class,
                    BuilderPattern.Product.class);
        }
    }

}
