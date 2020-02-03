package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.BuilderPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class BuilderPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public BuilderPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.Builder.class)) {
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.ConcreteBuilder.class)) {
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.Director.class)) {
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(BuilderPattern.Product.class)) {
        }
    }

}
