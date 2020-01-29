package org.jpatterns.validator;

import org.jpatterns.validator.gof.creational.AbstractFactoryPatternValidator;

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
import java.util.Set;

public class PatternValidatingAnnotationProcessor implements Processor {
    private ValidatorUtils validatorUtils;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        validatorUtils = new ValidatorUtils(processingEnv);
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
        new AbstractFactoryPatternValidator(validatorUtils).process(roundEnv);

        return false;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
                                                         ExecutableElement member, String userText) {
        return Collections.emptyList();
    }
}
