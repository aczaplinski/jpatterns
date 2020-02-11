package org.jpatterns.validator;

import org.jpatterns.validator.gof.behavioral.*;
import org.jpatterns.validator.gof.creational.*;
import org.jpatterns.validator.gof.structural.*;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PatternValidatingAnnotationProcessor implements Processor {
    private List<PatternValidator> patternValidators;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        ValidatorUtils validatorUtils = new ValidatorUtils(processingEnv);
        patternValidators = List.of(
                new ChainOfResponsibilityPatternValidator(validatorUtils),
                new CommandPatternValidator(validatorUtils),
                new InterpreterPatternValidator(validatorUtils),
                new IteratorPatternValidator(validatorUtils),
                new MediatorPatternValidator(validatorUtils),
                new MementoPatternValidator(validatorUtils),
                new ObserverPatternValidator(validatorUtils),
                new StatePatternValidator(validatorUtils),
                new StrategyPatternValidator(validatorUtils),
                new TemplateMethodPatternValidator(validatorUtils),
                new AbstractFactoryPatternValidator(validatorUtils),
                new BuilderPatternValidator(validatorUtils),
                new FactoryMethodPatternValidator(validatorUtils),
                new PrototypePatternValidator(validatorUtils),
                new SingletonPatternValidator(validatorUtils),
                new AdapterPatternValidator(validatorUtils),
                new BridgePatternValidator(validatorUtils),
                new CompositePatternValidator(validatorUtils),
                new DecoratorPatternValidator(validatorUtils),
                new FlyweightPatternValidator(validatorUtils),
                new ProxyPatternValidator(validatorUtils));
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of("*");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_13;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        patternValidators.forEach(patternValidator -> patternValidator.process(roundEnv));
        return false;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
                                                         ExecutableElement member, String userText) {
        return Collections.emptyList();
    }
}
