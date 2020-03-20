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
                        "       private Component decoratedComponent = new ConcreteComponent();",
                        "       @Override",
                        "       public void operation() {",
                        "           decoratedComponent.operation();",
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
        assertThat(compilation).hadWarningCount(3);
        assertThat(compilation).hadWarningContaining("ConcreteComponent should not be abstract");
        assertThat(compilation).hadWarningContaining("Decorator should be a subtype of");
        assertThat(compilation).hadWarningContaining("ConcreteDecorator should store Component");
    }

    @Test
    public void testComponentFieldIsClassTemplate() {
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
                        "   class ConcreteDecorator<T extends Component> implements Decorator {",
                        "       private T decoratedComponent;",
                        "       ConcreteDecorator(T decoratedComponent) {",
                        "           this.decoratedComponent = decoratedComponent;",
                        "       }",
                        "       @Override",
                        "       public void operation() {",
                        "           decoratedComponent.operation();",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testJavaIoFilterInputStream() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "import java.io.FilterInputStream;",
                        "import java.io.InputStream;",
                        "class Test {",
                        "   @DecoratorPattern.ConcreteDecorator",
                        "   class TestInputStream extends FilterInputStream {",
                        "       public TestInputStream(InputStream in) {",
                        "           super(in);",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
