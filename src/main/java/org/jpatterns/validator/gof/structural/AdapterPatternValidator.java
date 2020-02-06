package org.jpatterns.validator.gof.structural;

import org.jpatterns.gof.structural.AdapterPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class AdapterPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public AdapterPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AdapterPattern.Adapter.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    AdapterPattern.Adapter.class);
            validatorUtils.validateImplementsSomeInterface(annotatedElement,
                    AdapterPattern.Adapter.class);
        }
    }
}
