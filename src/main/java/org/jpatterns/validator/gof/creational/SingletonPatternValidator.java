package org.jpatterns.validator.gof.creational;

import org.jpatterns.core.ValidationErrorLevel;
import org.jpatterns.gof.creational.SingletonPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

public class SingletonPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public SingletonPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(SingletonPattern.Singleton.class)) {
            validatorUtils.validateTypeContainsElementAnnotatedWithAnyOf(annotatedElement,
                    SingletonPattern.Singleton.class,
                    SingletonPattern.SingletonMethod.class,
                    SingletonPattern.SingletonField.class);
            validateAllSingletonConstructorsArePrivate(annotatedElement);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(SingletonPattern.SingletonMethod.class)) {
            validatorUtils.validateElementModifiersContain(annotatedElement,
                    SingletonPattern.SingletonMethod.class,
                    Modifier.STATIC);
            validatorUtils.validateElementModifiersDoNotContain(annotatedElement,
                    SingletonPattern.SingletonMethod.class,
                    Modifier.PRIVATE, Modifier.PROTECTED);
            validatorUtils.validateMethodReturnsTypeAnnotatedWithAnyOf(annotatedElement,
                    SingletonPattern.SingletonMethod.class,
                    SingletonPattern.Singleton.class);
            validatorUtils.validateEnclosingTypeIsAnnotatedWithAnyOf(annotatedElement,
                    SingletonPattern.SingletonMethod.class,
                    SingletonPattern.Singleton.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(SingletonPattern.SingletonField.class)) {
            validatorUtils.validateElementModifiersContain(annotatedElement,
                    SingletonPattern.SingletonField.class,
                    Modifier.STATIC, Modifier.FINAL);
            validatorUtils.validateElementModifiersDoNotContain(annotatedElement,
                    SingletonPattern.SingletonField.class,
                    Modifier.PRIVATE, Modifier.PROTECTED);
            validatorUtils.validateFieldTypeIsAnnotatedWith(annotatedElement,
                    SingletonPattern.SingletonField.class,
                    SingletonPattern.Singleton.class);
            validatorUtils.validateEnclosingTypeIsAnnotatedWithAnyOf(annotatedElement,
                    SingletonPattern.SingletonField.class,
                    SingletonPattern.Singleton.class);
        }
    }

    private void validateAllSingletonConstructorsArePrivate(Element annotatedSingleton) {
        annotatedSingleton.getEnclosedElements().stream()
            .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR
                                && !element.getModifiers().contains(Modifier.PRIVATE))
            .forEach(element -> {
                        ValidationErrorLevel validationErrorLevel =
                                validatorUtils.getValidationErrorLevel(
                                        annotatedSingleton,
                                        SingletonPattern.Singleton.class);
                        if (validationErrorLevel != ValidationErrorLevel.NONE) {
                            validatorUtils.getMessager().printMessage(
                                    validationErrorLevel.getDiagnosticKind(),
                                    String.format(
                                            "Singleton's constructor %1$s be private",
                                            validationErrorLevel.getMessageVerb()),
                                    element);
                        }
                    }
            );
    }

}
