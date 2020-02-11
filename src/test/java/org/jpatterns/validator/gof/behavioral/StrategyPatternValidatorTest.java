package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class StrategyPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @StrategyPattern.Context",
                        "   class Context {",
                        "       @StrategyPattern.StrategyField",
                        "       private Strategy strategy = new ConcreteStrategy();",
                        "   }",
                        "   @StrategyPattern.Strategy",
                        "   abstract class Strategy {",
                        "   }",
                        "   @StrategyPattern.ConcreteStrategy",
                        "   class ConcreteStrategy extends Strategy {",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testStrategyFieldNotAnnotated() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @StrategyPattern.Context",
                        "   class Context {",
                        "       private Strategy strategy = new ConcreteStrategy();",
                        "   }",
                        "   @StrategyPattern.Strategy",
                        "   abstract class Strategy {",
                        "   }",
                        "   @StrategyPattern.ConcreteStrategy",
                        "   class ConcreteStrategy extends Strategy {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("Context should contain");
    }
}
