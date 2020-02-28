package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class ChainOfResponsibilityPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @ChainOfResponsibilityPattern.Handler",
                        "   interface Handler {",
                        "       void operation();",
                        "   }",
                        "   @ChainOfResponsibilityPattern.ConcreteHandler",
                        "   class Handler1 implements Handler {",
                        "       private Handler nextHandler;",
                        "       Handler1(Handler nextHandler){",
                        "           this.nextHandler = nextHandler;",
                        "       }",
                        "       @Override",
                        "       public void operation() {",
                        "           if(nextHandler != null) {",
                        "               nextHandler.operation();",
                        "           }",
                        "       }",
                        "   }",
                        "   @ChainOfResponsibilityPattern.ConcreteHandler",
                        "   class Handler2 implements Handler {",
                        "       private Handler nextHandler;",
                        "       Handler2(Handler nextHandler){",
                        "           this.nextHandler = nextHandler;",
                        "       }",
                        "       @Override",
                        "       public void operation() {",
                        "           if(nextHandler != null) {",
                        "               nextHandler.operation();",
                        "           }",
                        "       }",
                        "   }",
                        "   @ChainOfResponsibilityPattern.ConcreteHandler(defaultHandler = true)",
                        "   class Handler3 implements Handler {",
                        "       @Override",
                        "       public void operation() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testProxyNoAbstractHandler() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @ChainOfResponsibilityPattern.ConcreteHandler",
                        "   class Handler1 {",
                        "       private Handler1 nextHandler;",
                        "       Handler1(Handler1 nextHandler){",
                        "           this.nextHandler = nextHandler;",
                        "       }",
                        "       public void operation() {",
                        "           if(nextHandler != null) {",
                        "               nextHandler.operation();",
                        "           }",
                        "       }",
                        "   }",
                        "   @ChainOfResponsibilityPattern.ConcreteHandler",
                        "   class Handler2 {",
                        "       private Handler2 nextHandler;",
                        "       Handler2(Handler2 nextHandler){",
                        "           this.nextHandler = nextHandler;",
                        "       }",
                        "       public void operation() {",
                        "           if(nextHandler != null) {",
                        "               nextHandler.operation();",
                        "           }",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(4);
        assertThat(compilation).hadWarningContaining(
                "ConcreteHandler should be a subtype of");
        assertThat(compilation).hadWarningContaining(
                "ConcreteHandler that is not a default Handler should store Handler");
    }
}
