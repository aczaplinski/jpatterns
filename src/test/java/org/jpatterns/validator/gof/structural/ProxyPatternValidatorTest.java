package org.jpatterns.validator.gof.structural;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class ProxyPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @ProxyPattern.Subject",
                        "   interface Subject {",
                        "       void operation();",
                        "   }",
                        "   @ProxyPattern.RealSubject",
                        "   class RealSubject implements Subject {",
                        "       @Override",
                        "       public void operation() {",
                        "       }",
                        "   }",
                        "   @ProxyPattern.Proxy",
                        "   class Proxy implements Subject {",
                        "       private Subject proxied = new RealSubject();",
                        "       @Override",
                        "       public void operation() {",
                        "           proxied.operation();",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testProxyNotImplementingSubject() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @ProxyPattern.Subject",
                        "   interface Subject {",
                        "       void operation();",
                        "   }",
                        "   @ProxyPattern.RealSubject",
                        "   class RealSubject implements Subject {",
                        "       @Override",
                        "       public void operation() {",
                        "       }",
                        "   }",
                        "   @ProxyPattern.Proxy",
                        "   class Proxy {",
                        "       private Subject proxied = new RealSubject();",
                        "       public void operation() {",
                        "           proxied.operation();",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining(
                "Proxy should");
    }
}
