package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class StatePatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @StatePattern.State",
                        "   interface State {",
                        "   }",
                        "   @StatePattern.ConcreteState",
                        "   class ConcreteState implements State {",
                        "   }",
                        "   @StatePattern.Context",
                        "   class Context {",
                        "       private State state = new ConcreteState();",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testStateNotAbstract() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @StatePattern.State",
                        "   class State {",
                        "   }",
                        "   @StatePattern.ConcreteState",
                        "   class ConcreteState extends State {",
                        "   }",
                        "   @StatePattern.Context",
                        "   class Context {",
                        "       private State state = new ConcreteState();",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("State should be");
    }
}
