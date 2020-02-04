package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.SingletonPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class SingletonPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public SingletonPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(SingletonPattern.Singleton.class)) {
            validatorUtils.validateTypeContainsElementAnnotatedWith(annotatedElement,
                    SingletonPattern.Singleton.class,
                    SingletonPattern.SingletonMethod.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(SingletonPattern.SingletonMethod.class)) {
            validatorUtils.validateElementModifiersContain(annotatedElement,
                    SingletonPattern.SingletonMethod.class,
                    Modifier.STATIC);
            validatorUtils.validateElementModifiersDoNotContain(annotatedElement,
                    SingletonPattern.SingletonMethod.class,
                    Modifier.PRIVATE, Modifier.PROTECTED);
            validateSingletonMethodIsInsideSingleton(annotatedElement);
        }
    }

    private void validateSingletonMethodIsInsideSingleton(Element annotatedSingletonMethod) {
        if(annotatedSingletonMethod.getEnclosingElement()
                .getAnnotation(SingletonPattern.Singleton.class)
                == null) {
            validatorUtils.printMessage("Singleton Method %1$s reside in a class or interface" +
                            " annotated with @Singleton",
                    annotatedSingletonMethod,
                    SingletonPattern.SingletonMethod.class);
        }
    }

}
