package org.jpatterns.validator.gof.creational;

import org.jpatterns.gof.creational.AbstractFactoryPattern;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

public class AbstractFactoryPatternValidator {
    private ValidatorUtils validatorUtils;

    public AbstractFactoryPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.AbstractFactory.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    AbstractFactoryPattern.AbstractFactory.class);
            validateFactoryContainsFactoryMethod(annotatedElement);
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
            validateFactoryContainsFactoryMethod(annotatedElement);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.ConcreteProduct.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    AbstractFactoryPattern.ConcreteProduct.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(AbstractFactoryPattern.FactoryMethod.class)) {
            validatorUtils.validateElementModifiersDoNotContain(annotatedElement,
                    AbstractFactoryPattern.FactoryMethod.class,
                    Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC);
            validateFactoryMethodIsInsideFactory(annotatedElement);
            validateFactoryMethodReturnsProduct(annotatedElement);
        }
    }

    private void validateFactoryMethodIsInsideFactory(Element annotatedFactoryMethod) {
        if(annotatedFactoryMethod.getEnclosingElement()
                .getAnnotation(AbstractFactoryPattern.AbstractFactory.class)
                == null
                && annotatedFactoryMethod.getEnclosingElement()
                .getAnnotation(AbstractFactoryPattern.ConcreteFactory.class)
                == null) {
            validatorUtils.printMessage("Factory Method %1$s reside in a class or interface" +
                            " annotated with either @AbstractFactory or @ConcreteFactory",
                    annotatedFactoryMethod,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
    }

    private void validateFactoryContainsFactoryMethod(Element annotatedFactory) {
        if(annotatedFactory.getEnclosedElements()
                .stream()
                .noneMatch(potentialFactoryMethod ->
                        potentialFactoryMethod.getAnnotation(AbstractFactoryPattern.FactoryMethod.class) != null)) {
            validatorUtils.printMessage("Factory %1$s contain a method annotated with @FactoryMethod",
                    annotatedFactory,
                    AbstractFactoryPattern.AbstractFactory.class,
                    AbstractFactoryPattern.ConcreteFactory.class);
        }
    }

    private void validateFactoryMethodReturnsProduct(Element annotatedFactoryMethod) {
        Element returnedElement = validatorUtils.getTypes()
                .asElement(((ExecutableElement) annotatedFactoryMethod).getReturnType());
        if(returnedElement.getAnnotation(AbstractFactoryPattern.AbstractProduct.class) == null
                && returnedElement.getAnnotation(AbstractFactoryPattern.ConcreteProduct.class) == null) {
            validatorUtils.printMessage("Factory Method %1$s return a value of type annotated" +
                            " with @AbstractProduct or @ConcreteProduct",
                    annotatedFactoryMethod,
                    AbstractFactoryPattern.FactoryMethod.class);
        }
    }

}
