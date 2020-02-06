package org.jpatterns.validator.gof.creational;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class SingletonPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "import static org.jpatterns.core.ValidationErrorLevel.*;",
                        "class Test {",
                        "   @SingletonPattern.Singleton",
                        "   static class Singleton {",
                        "       private static final Singleton INSTANCE = new Singleton();",
                        "       private Singleton() {",
                        "       }",
                        "       @SingletonPattern.SingletonMethod(validationErrorLevel = ERROR)",
                        "       static Singleton singletonMethod() {",
                        "           return INSTANCE;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNoSingletonMethodError() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "import static org.jpatterns.core.ValidationErrorLevel.*;",
                        "class Test {",
                        "   @SingletonPattern.Singleton(validationErrorLevel = ERROR)",
                        "   static class Singleton {",
                        "       private static final Singleton INSTANCE = new Singleton();",
                        "       private Singleton() {",
                        "       }",
                        "       static Singleton notAnnotatedSingletonMethod() {",
                        "           return INSTANCE;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorCount(1);
        assertThat(compilation).hadErrorContaining("Singleton must contain");
    }

    @Test
    public void testSingletonMethodWrongModifiers() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "import static org.jpatterns.core.ValidationErrorLevel.*;",
                        "class Test {",
                        "   static class NotASingleton {",
                        "       @SingletonPattern.SingletonMethod(validationErrorLevel = ERROR)",
                        "       protected NotASingleton singletonMethod() {",
                        "           return null;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorCount(3);
        assertThat(compilation).hadErrorContaining("must be static");
        assertThat(compilation).hadErrorContaining("must not be protected");
        assertThat(compilation).hadErrorContaining("must reside in a class");
    }
}
