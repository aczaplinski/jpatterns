package org.jpatterns.validator.gof.creational;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class AbstractFactoryPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "HelloWorld",
                        "package org.jpatterns.gof.creational;",
                                "class HelloWorld {",
                                "   @AbstractFactoryPattern.AbstractFactory",
                                "   abstract class AbstractFactory {",
                                "       @AbstractFactoryPattern.FactoryMethod",
                                "       abstract AbstractProduct factoryMethod();",
                                "   }",
                                "   @AbstractFactoryPattern.ConcreteFactory",
                                "   class ConcreteFactory extends AbstractFactory {",
                                "       @AbstractFactoryPattern.FactoryMethod",
                                "       @Override",
                                "       ConcreteProduct factoryMethod() {",
                                "           return new ConcreteProduct();",
                                "       }",
                                "   }",
                                "   @AbstractFactoryPattern.AbstractProduct",
                                "   abstract class AbstractProduct {",
                                "   }",
                                "   @AbstractFactoryPattern.ConcreteProduct",
                                "   class ConcreteProduct extends AbstractProduct {",
                                "   }",
                                "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
