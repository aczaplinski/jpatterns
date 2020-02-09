package org.jpatterns.validator.gof.structural;

import org.jpatterns.gof.structural.ProxyPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class ProxyPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public ProxyPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ProxyPattern.Subject.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    ProxyPattern.Subject.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ProxyPattern.RealSubject.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    ProxyPattern.RealSubject.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    ProxyPattern.RealSubject.class,
                    ProxyPattern.RealSubject.class,
                    ProxyPattern.Subject.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(ProxyPattern.Proxy.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    ProxyPattern.Proxy.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    ProxyPattern.Proxy.class,
                    ProxyPattern.Proxy.class,
                    ProxyPattern.Subject.class);
        }
    }
}
