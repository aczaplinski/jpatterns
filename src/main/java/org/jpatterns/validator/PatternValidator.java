package org.jpatterns.validator;

import javax.annotation.processing.RoundEnvironment;

public interface PatternValidator {

    void process(RoundEnvironment roundEnv);
}
