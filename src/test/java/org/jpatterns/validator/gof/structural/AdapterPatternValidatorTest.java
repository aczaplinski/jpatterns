package org.jpatterns.validator.gof.structural;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class AdapterPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   interface Interface {",
                        "       void someMethod();",
                        "   }",
                        "   @AdapterPattern.Adapter",
                        "   class Adapter implements Interface {",
                        "       private Adaptee adaptee;",
                        "       Adapter(Adaptee adaptee) {",
                        "           this.adaptee = adaptee;",
                        "       }",
                        "       public void someMethod() {",
                        "           adaptee.methodToAdapt();",
                        "       }",
                        "   }",
                        "   @AdapterPattern.Adaptee",
                        "   class Adaptee {",
                        "       void methodToAdapt() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNotImplementingInterfaceWarning() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @AdapterPattern.Adapter",
                        "   class Adapter {",
                        "       private Adaptee adaptee;",
                        "       Adapter(Adaptee adaptee) {",
                        "           this.adaptee = adaptee;",
                        "       }",
                        "       public void someMethod() {",
                        "           adaptee.methodToAdapt();",
                        "       }",
                        "   }",
                        "   @AdapterPattern.Adaptee",
                        "   class Adaptee {",
                        "       void methodToAdapt() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("Adapter should implement");
    }
}
