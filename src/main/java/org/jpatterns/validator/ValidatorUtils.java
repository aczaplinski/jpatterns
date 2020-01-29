package org.jpatterns.validator;

import org.jpatterns.core.ValidationErrorLevel;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValidatorUtils {
    private Elements elements;
    private Messager messager;
    private Types types;

    public ValidatorUtils(ProcessingEnvironment processingEnv){
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        types = processingEnv.getTypeUtils();
    }

    public Elements getElements() {
        return elements;
    }

    public Types getTypes() {
        return types;
    }

    public void validateIsConcreteClass(Element annotatedType,
                                        Class<? extends Annotation> annotation) {
        if(annotatedType.getKind() == ElementKind.CLASS) {
            validateElementModifiersDoNotContain(annotatedType, annotation, Modifier.ABSTRACT);
        } else {
            printMessage(annotation.getSimpleName() + " %1$s not be an interface.",
                    annotatedType,
                    annotation);
        }
    }

    public void validateIsAbstractClassOrInterface(Element annotatedType,
                                                    Class<? extends Annotation> annotation) {
        if(annotatedType.getKind() == ElementKind.CLASS
                && !annotatedType.getModifiers().contains(Modifier.ABSTRACT)) {
            printMessage(annotation.getSimpleName() + " %1$s be an abstract class or an interface.",
                    annotatedType,
                    annotation);
        }
    }

    public void validateElementModifiersDoNotContain(Element annotatedElement,
                                                      Class<? extends Annotation> annotation,
                                                      Modifier ... modifiers) {
        Arrays.stream(modifiers)
                .filter(modifier -> annotatedElement.getModifiers().contains(modifier))
                .forEach(modifier -> printMessage(annotation.getSimpleName() + " %1$s not be " + modifier,
                        annotatedElement,
                        annotation));
    }

    /** Generates a compiler message of appropriate level.
     * Replaces all occurrences of "%1$s" in the message
     * with a verb like must or should.
     * annotations must contain a class representing an annotation present on
     * annotatedElement. The generated message will point to it.
     * @param message The message to be printed
     * @param annotatedElement The element the annotation the message concerns is on
     * @param annotations Array of Classes representing annotations, one of which is present on annotatedElement */
    @SafeVarargs
    public final void printMessage(String message,
                              Element annotatedElement,
                              Class<? extends Annotation> ... annotations) {
        Class<? extends Annotation> annotationPresent = Arrays.stream(annotations)
                .filter(annotation -> annotatedElement.getAnnotation(annotation) != null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Supplied Element is not annotated with any of the given annotations"));
        ValidationErrorLevel validationErrorLevel = getValidationErrorLevel(annotatedElement, annotationPresent);
        if(validationErrorLevel != ValidationErrorLevel.NONE) {
            messager.printMessage(validationErrorLevel.getDiagnosticKind(),
                    String.format(message, validationErrorLevel.getMessageVerb()),
                    annotatedElement,
                    getElementAnnotationMirror(annotatedElement,
                            annotationPresent));
        }
    }

    @SafeVarargs
    private AnnotationMirror getElementAnnotationMirror(Element annotatedElement,
                                                        Class<? extends Annotation> ... annotations) {
        List<String> annotationQualifiedNames = Arrays.stream(annotations)
                .map(annotation -> annotation.getName().replace('$', '.'))
                .collect(Collectors.toList());
        return elements.getAllAnnotationMirrors(annotatedElement)
                .stream()
                .filter(annotationMirror -> annotationQualifiedNames.contains(
                        annotationMirror
                                .getAnnotationType()
                                .toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Supplied Element is not annotated with any of the given annotations"));
    }

    private ValidationErrorLevel getValidationErrorLevel(Element annotatedElement,
                                                         Class<? extends  Annotation> annotation) {
        try {
            /* Annotations cannot extend nor implement anything, so there is no way to specify a common interface
                containing method validationErrorLevel(). That's why reflection is used here.
             */
            return (ValidationErrorLevel) annotation.getMethod("validationErrorLevel")
                    .invoke(annotatedElement.getAnnotation(annotation));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Could not retrieve validation error level", e);
        }
    }
}