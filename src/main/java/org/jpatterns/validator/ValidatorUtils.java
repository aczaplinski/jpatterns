package org.jpatterns.validator;

import org.jpatterns.core.ValidationErrorLevel;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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

    public Messager getMessager() {
        return messager;
    }

    public Types getTypes() {
        return types;
    }

    public void validateIsConcreteClass(Element annotatedType,
                                        Class<? extends Annotation> annotation) {
        if(annotatedType.getKind().isClass()) {
            validateElementModifiersDoNotContain(annotatedType, annotation, Modifier.ABSTRACT);
        } else {
            printMessage(annotation.getSimpleName() + " %1$s not be an interface.",
                    annotatedType,
                    annotation);
        }
    }

    public void validateIsAbstractClassOrInterface(Element annotatedType,
                                                    Class<? extends Annotation> annotation) {
        if(annotatedType.getKind().isClass()
                && !annotatedType.getModifiers().contains(Modifier.ABSTRACT)) {
            printMessage(annotation.getSimpleName() + " %1$s be an abstract class or an interface.",
                    annotatedType,
                    annotation);
        }
    }

    public void validateElementModifiersContain(Element annotatedElement,
                                                Class<? extends Annotation> annotation,
                                                Modifier ... modifiers) {
        Arrays.stream(modifiers)
                .filter(modifier -> !annotatedElement.getModifiers().contains(modifier))
                .forEach(modifier -> printMessage(
                        annotation.getSimpleName() + " %1$s be " + modifier + ".",
                        annotatedElement,
                        annotation));
    }

    public void validateElementModifiersDoNotContain(Element annotatedElement,
                                                     Class<? extends Annotation> annotation,
                                                     Modifier ... modifiers) {
        Arrays.stream(modifiers)
                .filter(modifier -> annotatedElement.getModifiers().contains(modifier))
                .forEach(modifier -> printMessage(
                        annotation.getSimpleName() + " %1$s not be " + modifier + ".",
                        annotatedElement,
                        annotation));
    }

    @SafeVarargs
    public final void validateSomeSupertypeIsAnnotatedWithAnyOf(
            Element annotatedType,
            Class<? extends Annotation> annotation,
            Class<? extends Annotation> ... allowedSupertypeAnnotations) {
        if(types.directSupertypes(annotatedType.asType()).stream()
                .noneMatch(superTypeMirror -> isAnnotatedWithAnyOf(
                        superTypeMirror, allowedSupertypeAnnotations))) {
            printMessage(annotation.getSimpleName()
                            + " %1$s be a subtype of one of: "
                            + toString(allowedSupertypeAnnotations)
                            + ".",
                    annotatedType,
                    annotation);
        }
    }

    public void validateImplementsSomeInterface(Element annotatedType, Class<? extends Annotation> annotation) {
        if(types.directSupertypes(annotatedType.asType())
                .stream()
                .noneMatch(typeMirror -> types.asElement(typeMirror)
                        .getKind() == ElementKind.INTERFACE)) {
            printMessage(annotation.getSimpleName()
                            + " %1$s implement some interface.",
                    annotatedType,
                    annotation);
        }
    }

    @SafeVarargs
    public final void validateTypeContainsElementAnnotatedWithAnyOf(Element annotatedElement,
                                                              Class<? extends Annotation> annotation,
                                                              Class<? extends Annotation> ... subelementAnnotations) {
        if(annotatedElement.getEnclosedElements()
                .stream()
                .noneMatch(potentialAnnotatedSubelement ->
                        isAnnotatedWithAnyOf(potentialAnnotatedSubelement, subelementAnnotations))) {
            printMessage(annotation.getSimpleName() +
                            " %1$s contain " +
                            (subelementAnnotations.length == 1 ? "a " : "one of: ") +
                            toString(subelementAnnotations) +
                            ".",
                    annotatedElement,
                    annotation);
        }
    }

    @SafeVarargs
    public final void validateEnclosingTypeIsAnnotatedWithAnyOf(Element annotatedElement,
                                                     Class<? extends Annotation> annotation,
                                                     Class<? extends Annotation> ... allowedTypeAnnotations) {
        if(Arrays.stream(allowedTypeAnnotations)
            .allMatch(allowedTypeAnnotation ->
                    annotatedElement.getEnclosingElement().getAnnotation(allowedTypeAnnotation) == null)) {
            printMessage(annotation.getSimpleName() +
                            " %1$s reside in a class or interface annotated with " +
                            (allowedTypeAnnotations.length == 1 ? "" : "any of: ") +
                            toString(allowedTypeAnnotations),
                    annotatedElement,
                    annotation);
        }
    }

    @SafeVarargs
    public final void validateContainsMethodReturningTypeAnnotatedWithAnyOf(Element annotatedElement,
                                                             Class<? extends Annotation> annotation,
                                                             Class<? extends Annotation> ... returnTypeAnnotations) {
        if(!containsMethodReturningTypeAnnotatedWithAnyOf(annotatedElement, returnTypeAnnotations)) {
            printMessage(
                    annotation.getSimpleName() +
                            " %1$s contain a method returning " +
                            (returnTypeAnnotations.length == 1 ? "" : "one of: ") +
                            toString(returnTypeAnnotations) +
                            ".",
                    annotatedElement,
                    annotation);
        }
    }

    @SafeVarargs
    public final void validateMethodReturnsTypeAnnotatedWithAnyOf(Element methodElement,
                                                            Class<? extends Annotation> annotation,
                                                            Class<? extends Annotation> ... returnTypeAnnotations) {
        if(!isMethodReturningTypeAnnotatedWithAnyOf(methodElement, returnTypeAnnotations)) {
            printMessage(annotation.getSimpleName() +
                            " %1$s return a value of type annotated" +
                            " with " +
                            (returnTypeAnnotations.length == 1 ? "" : "one of: ") +
                            toString(returnTypeAnnotations) +
                            ".",
                    methodElement,
                    annotation);
        }
    }

    @SafeVarargs
    public final void validateContainsFieldOfTypeAnnotatedWithAnyOf(Element element,
                                                              Class<? extends Annotation> annotation,
                                                              Class<? extends Annotation> ... soughtTypeAnnotations) {
        if(!containsFieldOfTypeAnnotatedWithAnyOf(element, soughtTypeAnnotations)) {
            printMessage(annotation.getSimpleName() +
                            " %1$s store " +
                            (soughtTypeAnnotations.length == 1 ? "" : "one of: ") +
                            toString(soughtTypeAnnotations) +
                            " reference.",
                    element,
                    annotation);
        }
    }

    public void validateFieldTypeIsAnnotatedWith(Element fieldElement,
                                                 Class<? extends Annotation> annotation,
                                                 Class<? extends Annotation> soughtTypeAnnotation) {
        if(!isAnnotatedWithAnyOf(fieldElement.asType(), soughtTypeAnnotation)) {
            printMessage(annotation.getSimpleName() +
                         "'s type %1$s be annotated with " +
                         soughtTypeAnnotation.getSimpleName(),
                    fieldElement,
                    annotation);
        }
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

    public Element getReturnedElement(ExecutableElement methodExecutableElement) {
        return types.asElement(methodExecutableElement.getReturnType());
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

    public ValidationErrorLevel getValidationErrorLevel(Element annotatedElement,
                                                        Class<? extends Annotation> annotation) {
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

    @SafeVarargs
    private boolean isAnnotatedWithAnyOf(TypeMirror typeMirror, Class<? extends Annotation> ... annotations) {
        /* typeMirror.getAnnotation(<Class<A>>) returns null even if the annotation is present,
           so is it converted to Element and the Element taking version of the method is used */
        Element element = types.asElement(typeMirror);
        if(element == null) {
            return false;
        }
        if(isAnnotatedWithAnyOf(element, annotations)) {
            return true;
        }
        if(typeMirror instanceof TypeVariable) {
            /* If there is for example a class B<T extends C>{T t;}, C is annotated with @A,
             and we ask whether t is annotated with @A,
             then this if tells yes. */
            return types.directSupertypes(typeMirror).stream()
                    .anyMatch(typeVariableUpperBoundCandidate ->
                            isAnnotatedWithAnyOf(typeVariableUpperBoundCandidate, annotations));
        }
        return false;
    }

    private boolean isAnnotatedWithAnyOf(Element element, Class<? extends Annotation>[] annotations) {
        Set<Class<? extends Annotation>> elementHardcodedAnnotations =
                HardcodedAnnotations
                        .HARDCODED_ANNOTATIONS
                        .getOrDefault(element.toString(), Collections.emptySet());
        return Arrays.stream(annotations)
                .anyMatch(annotation -> elementHardcodedAnnotations.contains(annotation)
                                        || element.getAnnotation(annotation) != null);
    }

    private boolean isMethodReturningTypeAnnotatedWithAnyOf(Element element,
                                                   Class<? extends Annotation>[] returnTypeAnnotations) {
        return element.getKind() == ElementKind.METHOD
                && Optional.ofNullable(getReturnedElement((ExecutableElement) element))
                .map(returnedElement -> isAnnotatedWithAnyOf(returnedElement, returnTypeAnnotations))
                .orElse(false);
    }

    private boolean containsMethodReturningTypeAnnotatedWithAnyOf(Element element,
                                                                 Class<? extends Annotation>[] returnTypeAnnotations) {
        return element.getEnclosedElements().stream()
                .anyMatch(candidateMethod ->
                        isMethodReturningTypeAnnotatedWithAnyOf(candidateMethod, returnTypeAnnotations))
                ||
                types.directSupertypes(element.asType()).stream()
                        .anyMatch(supertypeCandidate ->
                                containsMethodReturningTypeAnnotatedWithAnyOf(
                                        types.asElement(
                                                supertypeCandidate),
                                        returnTypeAnnotations));
    }

    @SafeVarargs
    public final boolean containsFieldOfTypeAnnotatedWithAnyOf(Element element,
                                                         Class<? extends Annotation> ... soughtTypeAnnotations) {
        return element.getEnclosedElements().stream()
                .anyMatch(candidateField ->
                        candidateField.getKind() == ElementKind.FIELD &&
                                isAnnotatedWithAnyOf(candidateField.asType(),
                                        soughtTypeAnnotations))
                ||
                types.directSupertypes(element.asType()).stream()
                        .anyMatch(supertypeCandidate ->
                                containsFieldOfTypeAnnotatedWithAnyOf(
                                        types.asElement(
                                                supertypeCandidate),
                                        soughtTypeAnnotations));
    }

    private String toString(Class<? extends Annotation>[] annotations) {
        return Arrays.stream(annotations)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
    }
}
