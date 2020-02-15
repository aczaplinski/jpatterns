package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.AbstractFactoryPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

public class AbstractFactoryPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public AbstractFactoryPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractFactory.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    AbstractFactoryPattern.AbstractFactory.class);
            validatorUtils.validateTypeContainsElementAnnotatedWith(annotatedElement,
                    AbstractFactoryPattern.AbstractFactory.class,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractProduct.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    AbstractFactoryPattern.AbstractProduct.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteFactory.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    AbstractFactoryPattern.ConcreteFactory.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    AbstractFactoryPattern.ConcreteFactory.class,
                    AbstractFactoryPattern.ConcreteFactory.class,
                    AbstractFactoryPattern.AbstractFactory.class);
            validatorUtils.validateTypeContainsElementAnnotatedWith(annotatedElement,
                    AbstractFactoryPattern.ConcreteFactory.class,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteProduct.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    AbstractFactoryPattern.ConcreteProduct.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    AbstractFactoryPattern.ConcreteProduct.class,
                    AbstractFactoryPattern.ConcreteProduct.class,
                    AbstractFactoryPattern.AbstractProduct.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.FactoryMethod.class)) {
            validatorUtils.validateElementModifiersDoNotContain(annotatedElement,
                    AbstractFactoryPattern.FactoryMethod.class,
                    Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC);
            validatorUtils.validateEnclosingTypeIsAnnotatedWithAnyOf(annotatedElement,
                    AbstractFactoryPattern.FactoryMethod.class,
                    AbstractFactoryPattern.AbstractFactory.class,
                    AbstractFactoryPattern.ConcreteFactory.class);
            validateFactoryIsAbstractOrFactoryMethodHasOverride(annotatedElement);
            validateFactoryMethodReturnsProduct(annotatedElement);
        }
    }

    private void validateFactoryIsAbstractOrFactoryMethodHasOverride(Element annotatedFactoryMethod) {
        if(annotatedFactoryMethod.getEnclosingElement()
                .getAnnotation(AbstractFactoryPattern.ConcreteFactory.class)
                != null
        && annotatedFactoryMethod.getAnnotation(Override.class) == null) {
            validatorUtils.printMessage("Factory Method in Concrete Factory %1$s override" +
                    " a Factory Method from its superclass, so it %1$s also be annotated with @Override",
                    annotatedFactoryMethod,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
    }

    private void validateFactoryMethodReturnsProduct(Element annotatedFactoryMethod) {
        Element returnedElement = validatorUtils.getReturnedElement(
                (ExecutableElement) annotatedFactoryMethod);
        if(returnedElement == null
                || (returnedElement.getAnnotation(AbstractFactoryPattern.AbstractProduct.class) == null
                    && returnedElement.getAnnotation(AbstractFactoryPattern.ConcreteProduct.class) == null)) {
            validatorUtils.printMessage("Factory Method %1$s return a value of type annotated" +
                            " with @AbstractProduct or @ConcreteProduct",
                    annotatedFactoryMethod,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
    }

}
