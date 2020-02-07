package org.jpatterns.validator.gof.structural;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class CompositePatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @CompositePattern.Component",
                        "   interface Component {",
                        "       void operation();",
                        "   }",
                        "   @CompositePattern.Composite",
                        "   class Composite implements Component {",
                        "       private Component component1, component2;",
                        "       public Composite(Component component1, Component component2) {",
                        "           this.component1 = component1;",
                        "           this.component2 = component2;",
                        "       }",
                        "       @Override",
                        "       public void operation() {",
                        "           component1.operation();",
                        "           component2.operation();",
                        "       }",
                        "   }",
                        "   @CompositePattern.Leaf",
                        "   class Leaf implements Component {",
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
                        "   @CompositePattern.Component",
                        "   interface Component {",
                        "       void operation();",
                        "   }",
                        "   @CompositePattern.Composite",
                        "   class Composite {",
                        "       private Component component1, component2;",
                        "       public Composite(Component component1, Component component2) {",
                        "           this.component1 = component1;",
                        "           this.component2 = component2;",
                        "       }",
                        "       public void operation() {",
                        "           component1.operation();",
                        "           component2.operation();",
                        "       }",
                        "   }",
                        "   @CompositePattern.Leaf",
                        "   abstract class Leaf implements Component {",
                        "       @Override",
                        "       public void operation() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("Leaf should not be abstract");
        assertThat(compilation).hadWarningContaining("Composite should be a subtype of");
    }
}
