package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class MementoPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @MementoPattern.Memento",
                        "   interface Memento {",
                        "       Object getState();",
                        "   }",
                        "   @MementoPattern.Originator",
                        "   static class Originator {",
                        "       Memento createMemento() {",
                        "           return new MementoImpl();",
                        "       }",
                        "       class MementoImpl implements Memento {",
                        "           @Override",
                        "           public Object getState() {",
                        "               return new Object();",
                        "           }",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testOriginatorWithNoMethodReturningMemento() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @MementoPattern.Memento",
                        "   interface Memento {",
                        "       Object getState();",
                        "   }",
                        "   @MementoPattern.Originator",
                        "   static class Originator {",
                        "       class MementoImpl implements Memento {",
                        "           @Override",
                        "           public Object getState() {",
                        "               return new Object();",
                        "           }",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("Originator should");
    }
}
