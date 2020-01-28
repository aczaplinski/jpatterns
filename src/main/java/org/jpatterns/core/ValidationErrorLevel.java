package org.jpatterns.core;

import javax.tools.Diagnostic;

public enum ValidationErrorLevel {
    ERROR(Diagnostic.Kind.ERROR, "must"),
    WARNING(Diagnostic.Kind.WARNING, "should"),
    NOTE(Diagnostic.Kind.NOTE, "should"),
    NONE(null, null);

    private final Diagnostic.Kind diagnosticKind;
    private final String messageVerb;

    ValidationErrorLevel(Diagnostic.Kind diagnosticKind, String messageVerb) {
        this.diagnosticKind = diagnosticKind;
        this.messageVerb = messageVerb;
    }

    public Diagnostic.Kind getDiagnosticKind() {
        return diagnosticKind;
    }

    /** @return Verb for usage in error messages, e. g. must */
    public String getMessageVerb() {
        return messageVerb;
    }
}
