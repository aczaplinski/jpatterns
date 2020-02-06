package org.jpatterns.validator.gof.creational;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class FactoryMethodPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @FactoryMethodPattern.Creator",
                        "   abstract class Creator {",
                        "       abstract Product factoryMethod();",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteCreator",
                        "   class ConcreteCreator extends Creator {",
                        "       @Override",
                        "       ConcreteProduct factoryMethod() {",
                        "           return new ConcreteProduct();",
                        "       }",
                        "   }",
                        "   @FactoryMethodPattern.Product",
                        "   abstract class Product {",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteProduct",
                        "   class ConcreteProduct extends Product {",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNoFactoryMethodWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @FactoryMethodPattern.Creator",
                        "   abstract class Creator {",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteCreator",
                        "   class ConcreteCreator extends Creator {",
                        "   }",
                        "   @FactoryMethodPattern.Product",
                        "   abstract class Product {",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteProduct",
                        "   class ConcreteProduct extends Product {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("Creator should contain");
        assertThat(compilation).hadWarningContaining("ConcreteCreator should contain");
    }

    @Test
    public void testNoAbstractSuperclassWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @FactoryMethodPattern.ConcreteCreator",
                        "   class ConcreteCreator extends java.util.ArrayList<java.lang.String> {",
                        "       ConcreteProduct factoryMethod() {",
                        "           return new ConcreteProduct();",
                        "       }",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteProduct",
                        "   class ConcreteProduct {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("ConcreteCreator should be a subtype of");
        assertThat(compilation).hadWarningContaining("ConcreteProduct should be a subtype of");
    }

    @Test
    public void testFactoryMethodNotReturningProductWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @FactoryMethodPattern.Creator",
                        "   abstract class Creator {",
                        "       abstract Product factoryMethod();",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteCreator",
                        "   class ConcreteCreator extends Creator {",
                        "       @Override",
                        "       ConcreteProduct factoryMethod() {",
                        "           return new ConcreteProduct();",
                        "       }",
                        "   }",
                        "   abstract class Product {",
                        "   }",
                        "   class ConcreteProduct extends Product {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("should contain a method");
    }

    @Test
    public void testMulitpleWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @FactoryMethodPattern.Creator",
                        "   class Creator {",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteCreator",
                        "   class ConcreteCreator extends Creator {",
                        "       ConcreteProduct factoryMethod() {",
                        "           return null;",
                        "       }",
                        "   }",
                        "   @FactoryMethodPattern.Product",
                        "   interface Product {",
                        "   }",
                        "   @FactoryMethodPattern.ConcreteProduct",
                        "   interface ConcreteProduct extends Product {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(3);
        assertThat(compilation).hadWarningContaining("should not be an interface");
        assertThat(compilation).hadWarningContaining("should be an abstract class");
        assertThat(compilation).hadWarningContaining("should contain a method returning");
    }
}
