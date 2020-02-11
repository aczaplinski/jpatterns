package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class VisitorPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @VisitorPattern.Visitor",
                        "   abstract class Visitor {",
                        "      abstract  void visit(ConcreteElement concreteElement);",
                        "   }",
                        "   @VisitorPattern.ConcreteVisitor",
                        "   class ConcreteVisitor extends Visitor {",
                        "       @Override",
                        "       void visit(ConcreteElement concreteElement) {",
                        "       }",
                        "   }",
                        "   @VisitorPattern.Element",
                        "   abstract class Element {",
                        "      abstract void accept(Visitor visitor);",
                        "   }",
                        "   @VisitorPattern.ConcreteElement",
                        "   class ConcreteElement extends Element {",
                        "       @Override",
                        "       void accept(Visitor visitor) {",
                        "           visitor.visit(this);",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testAbstractConcreteVisitor() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @VisitorPattern.Visitor",
                        "   abstract class Visitor {",
                        "      abstract  void visit(ConcreteElement concreteElement);",
                        "   }",
                        "   @VisitorPattern.ConcreteVisitor",
                        "   abstract class ConcreteVisitor extends Visitor {",
                        "       @Override",
                        "       void visit(ConcreteElement concreteElement) {",
                        "       }",
                        "   }",
                        "   @VisitorPattern.Element",
                        "   abstract class Element {",
                        "      abstract void accept(Visitor visitor);",
                        "   }",
                        "   @VisitorPattern.ConcreteElement",
                        "   class ConcreteElement extends Element {",
                        "       @Override",
                        "       void accept(Visitor visitor) {",
                        "           visitor.visit(this);",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("ConcreteVisitor should");
    }
}
