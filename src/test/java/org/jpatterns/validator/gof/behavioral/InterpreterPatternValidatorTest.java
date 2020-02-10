package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class InterpreterPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @InterpreterPattern.Context",
                        "   class Context {",
                        "   }",
                        "   @InterpreterPattern.AbstractExpression",
                        "   interface AbstractExpression {",
                        "       void interpret(Context context);",
                        "   }",
                        "   @InterpreterPattern.TerminalExpression",
                        "   class TerminalExpression implements AbstractExpression {",
                        "       @Override",
                        "       public void interpret(Context context) {",
                        "       }",
                        "   }",
                        "   @InterpreterPattern.NonterminalExpression",
                        "   class NonterminalExpression implements AbstractExpression {",
                        "       private AbstractExpression expression1 = new TerminalExpression();",
                        "       private AbstractExpression expression2 = new TerminalExpression();",
                        "       @Override",
                        "       public void interpret(Context context) {",
                        "           expression1.interpret(context);",
                        "           expression2.interpret(context);",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testAllClassesAbstract() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @InterpreterPattern.Context",
                        "   abstract class Context {",
                        "   }",
                        "   @InterpreterPattern.AbstractExpression",
                        "   interface AbstractExpression {",
                        "       void interpret(Context context);",
                        "   }",
                        "   @InterpreterPattern.TerminalExpression",
                        "   abstract class TerminalExpression implements AbstractExpression {",
                        "       @Override",
                        "       public void interpret(Context context) {",
                        "       }",
                        "   }",
                        "   @InterpreterPattern.NonterminalExpression",
                        "   abstract class NonterminalExpression implements AbstractExpression {",
                        "       private AbstractExpression expression1;",
                        "       private AbstractExpression expression2;",
                        "       @Override",
                        "       public void interpret(Context context) {",
                        "           expression1.interpret(context);",
                        "           expression2.interpret(context);",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(3);
        assertThat(compilation).hadWarningContaining("Context should not be");
        assertThat(compilation).hadWarningContaining("TerminalExpression should not be");
        assertThat(compilation).hadWarningContaining("NonterminalExpression should not be");
    }
}
