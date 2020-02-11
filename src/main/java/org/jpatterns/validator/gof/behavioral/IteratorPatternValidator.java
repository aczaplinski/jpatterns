package org.jpatterns.validator.gof.behavioral;

import org.jpatterns.gof.behavioral.IteratorPattern;
import org.jpatterns.validator.PatternValidator;
import org.jpatterns.validator.ValidatorUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class IteratorPatternValidator implements PatternValidator {
    private ValidatorUtils validatorUtils;

    public IteratorPatternValidator(ValidatorUtils validatorUtils) {
        this.validatorUtils = validatorUtils;
    }

    @Override
    public void process(RoundEnvironment roundEnv) {
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(IteratorPattern.Aggregate.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    IteratorPattern.Aggregate.class);
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    IteratorPattern.Aggregate.class,
                    IteratorPattern.Iterator.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(IteratorPattern.ConcreteAggregate.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    IteratorPattern.ConcreteAggregate.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    IteratorPattern.ConcreteAggregate.class,
                    IteratorPattern.ConcreteAggregate.class,
                    IteratorPattern.Aggregate.class);
            validatorUtils.validateContainsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement,
                    IteratorPattern.ConcreteAggregate.class,
                    IteratorPattern.Iterator.class,
                    IteratorPattern.ConcreteIterator.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(IteratorPattern.Iterator.class)) {
            validatorUtils.validateIsAbstractClassOrInterface(annotatedElement,
                    IteratorPattern.Iterator.class);
        }
        for(Element annotatedElement :
                roundEnv.getElementsAnnotatedWith(IteratorPattern.ConcreteIterator.class)) {
            validatorUtils.validateIsConcreteClass(annotatedElement,
                    IteratorPattern.ConcreteIterator.class);
            validatorUtils.validateSomeSupertypeIsAnnotatedWithAnyOf(annotatedElement,
                    IteratorPattern.ConcreteIterator.class,
                    IteratorPattern.ConcreteIterator.class,
                    IteratorPattern.Iterator.class);
        }
    }
}
