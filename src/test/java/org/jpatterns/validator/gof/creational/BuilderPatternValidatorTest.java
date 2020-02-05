package org.jpatterns.validator.gof.creational;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class BuilderPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @BuilderPattern.Product",
                        "   class Product extends java.util.ArrayList<java.lang.String> {",
                        "   }",
                        "   @BuilderPattern.Builder",
                        "   abstract class Builder {",
                        "       abstract Product get();",
                        "   }",
                        "   @BuilderPattern.ConcreteBuilder",
                        "   class ConcreteBuilder extends Builder {",
                        "       Product get() {",
                        "           return new Product();",
                        "       }",
                        "   }",
                        "   @BuilderPattern.Director",
                        "   class Director {",
                        "       Product create() {",
                        "           return new ConcreteBuilder().get();",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNotReturningProductWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @BuilderPattern.Product",
                        "   class Product extends java.util.ArrayList<java.lang.String> {",
                        "   }",
                        "   @BuilderPattern.Builder",
                        "   abstract class Builder {",
                        "       abstract void get();",
                        "   }",
                        "   @BuilderPattern.ConcreteBuilder",
                        "   class ConcreteBuilder extends Builder {",
                        "       void get() {",
                        "       }",
                        "   }",
                        "   @BuilderPattern.Director",
                        "   class Director {",
                        "       java.util.ArrayList<java.lang.String> create() {",
                        "           return new Product();",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(3);
        assertThat(compilation).hadWarningContaining("should contain a method returning Product");
    }
}
