package org.jpatterns.validator.gof.structural;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class DecoratorPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @DecoratorPattern.Component",
                        "   interface Component {",
                        "       void operation();",
                        "   }",
                        "   @DecoratorPattern.ConcreteComponent",
                        "   class ConcreteComponent implements Component {",
                        "       @Override",
                        "       public void operation() {",
                        "       }",
                        "   }",
                        "   @DecoratorPattern.Decorator",
                        "   interface Decorator extends Component {",
                        "   }",
                        "   @DecoratorPattern.ConcreteDecorator",
                        "   class ConcreteDecorator implements Decorator {",
                        "       @Override",
                        "       public void operation() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testMultipleWarnings() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @DecoratorPattern.Component",
                        "   interface Component {",
                        "       void operation();",
                        "   }",
                        "   @DecoratorPattern.ConcreteComponent",
                        "   abstract class ConcreteComponent implements Component {",
                        "   }",
                        "   @DecoratorPattern.Decorator",
                        "   interface Decorator {",
                        "   }",
                        "   @DecoratorPattern.ConcreteDecorator",
                        "   class ConcreteDecorator implements Decorator {",
                        "       public void operation() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("ConcreteComponent should not be abstract");
        assertThat(compilation).hadWarningContaining("Decorator should be a subtype of");
    }
}
