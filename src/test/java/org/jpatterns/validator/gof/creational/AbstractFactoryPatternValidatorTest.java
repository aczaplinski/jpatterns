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
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
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

    @Test
    public void testNoFactoryMethodWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @AbstractFactoryPattern.AbstractFactory",
                        "   abstract class AbstractFactory {",
                        "   }",
                        "   @AbstractFactoryPattern.ConcreteFactory",
                        "   class ConcreteFactory extends AbstractFactory {",
                        "   }",
                        "   @AbstractFactoryPattern.AbstractProduct",
                        "   abstract class AbstractProduct {",
                        "   }",
                        "   @AbstractFactoryPattern.ConcreteProduct",
                        "   class ConcreteProduct extends AbstractProduct {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("AbstractFactory should contain");
        assertThat(compilation).hadWarningContaining("ConcreteFactory should contain");
    }

    @Test
    public void testNoAbstractSuperclassErrors() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "import static org.jpatterns.core.ValidationErrorLevel.*;",
                        "class Test {",
                        "   @AbstractFactoryPattern.ConcreteFactory(",
                        "       validationErrorLevel = ERROR)",
                        "   class ConcreteFactory {",
                        "       @AbstractFactoryPattern.FactoryMethod(",
                        "           validationErrorLevel = ERROR)",
                        "       ConcreteProduct factoryMethod() {",
                        "           return new ConcreteProduct();",
                        "       }",
                        "   }",
                        "   @AbstractFactoryPattern.ConcreteProduct(",
                        "       validationErrorLevel = ERROR)",
                        "   class ConcreteProduct {",
                        "   }",
                        "}"));
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorCount(3);
        assertThat(compilation).hadErrorContaining("ConcreteFactory must be a subtype of");
        assertThat(compilation).hadErrorContaining("ConcreteProduct must be a subtype of");
        assertThat(compilation).hadErrorContaining("Factory Method");
        assertThat(compilation).hadErrorContaining("must override");
    }

    @Test
    public void testFactoryMethodNotReturningProductNote() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "import static org.jpatterns.core.ValidationErrorLevel.*;",
                        "class Test {",
                        "   @AbstractFactoryPattern.AbstractFactory",
                        "   interface AbstractFactory {",
                        "       @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = NOTE)",
                        "       Object factoryMethod();",
                        "   }",
                        "   @AbstractFactoryPattern.ConcreteFactory",
                        "   class ConcreteFactory implements AbstractFactory {",
                        "       @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = NOTE)",
                        "       @Override",
                        "       public String factoryMethod() {",
                        "           return \"Some String\";",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
        assertThat(compilation).hadNoteCount(2);
        assertThat(compilation).hadNoteContaining("should return a value of type");
    }

    @Test
    public void testNoInfoIfRequested() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "import static org.jpatterns.core.ValidationErrorLevel.*;",
                        "class Test {",
                        "   @AbstractFactoryPattern.AbstractFactory(validationErrorLevel = NONE)",
                        "   class AbstractFactory {",
                        "   }",
                        "   class ConcreteFactory extends AbstractFactory {",
                        "       @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = NONE)",
                        "       private ConcreteProduct factoryMethod() {",
                        "           return null;",
                        "       }",
                        "   }",
                        "   @AbstractFactoryPattern.AbstractProduct(validationErrorLevel = NONE)",
                        "   class AbstractProduct {",
                        "   }",
                        "   @AbstractFactoryPattern.ConcreteProduct(validationErrorLevel = NONE)",
                        "   interface ConcreteProduct {",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
        assertThat(compilation).hadNoteCount(0);
    }

    @Test
    public void testMultipleWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "import static org.jpatterns.core.ValidationErrorLevel.*;",
                        "class Test {",
                        "   @AbstractFactoryPattern.AbstractFactory(validationErrorLevel = WARNING)",
                        "   class AbstractFactory {",
                        "   }",
                        "   class ConcreteFactory extends AbstractFactory {",
                        "       @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = WARNING)",
                        "       private ConcreteProduct factoryMethod() {",
                        "           return null;",
                        "       }",
                        "   }",
                        "   @AbstractFactoryPattern.AbstractProduct(validationErrorLevel = WARNING)",
                        "   class AbstractProduct {",
                        "   }",
                        "   @AbstractFactoryPattern.ConcreteProduct(validationErrorLevel = WARNING)",
                        "   interface ConcreteProduct {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(7);
        assertThat(compilation).hadWarningContaining("should be an abstract class");
        assertThat(compilation).hadWarningContaining("should contain a FactoryMethod");
        assertThat(compilation).hadWarningContaining("should not be an interface");
        assertThat(compilation).hadWarningContaining("should be a subtype of");
        assertThat(compilation).hadWarningContaining("should not be private");
        assertThat(compilation).hadWarningContaining("should reside in");
    }
}
