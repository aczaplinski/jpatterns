package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class TemplateMethodPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @TemplateMethodPattern.AbstractClass",
                        "   abstract class AbstractClass {",
                        "       @TemplateMethodPattern.TemplateMethod",
                        "       final void templateMethod() {",
                        "           primitiveOperation();",
                        "       }",
                        "       @TemplateMethodPattern.PrimitiveOperation",
                        "       abstract void primitiveOperation();",
                        "   }",
                        "   @TemplateMethodPattern.ConcreteClass",
                        "   class ConcreteClass extends AbstractClass {",
                        "       @Override",
                        "       @TemplateMethodPattern.PrimitiveOperation",
                        "       void primitiveOperation() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNoMethods() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @TemplateMethodPattern.AbstractClass",
                        "   abstract class AbstractClass {",
                        "   }",
                        "   @TemplateMethodPattern.ConcreteClass",
                        "   class ConcreteClass extends AbstractClass {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(3);
        assertThat(compilation).hadWarningContaining("AbstractClass should contain");
        assertThat(compilation).hadWarningContaining("ConcreteClass should contain");
    }

    @Test
    public void testOperationMethodInConcreteClassWithoutOverride() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @TemplateMethodPattern.AbstractClass",
                        "   abstract class AbstractClass {",
                        "       @TemplateMethodPattern.TemplateMethod",
                        "       final void templateMethod() {",
                        "           primitiveOperation();",
                        "       }",
                        "       @TemplateMethodPattern.PrimitiveOperation",
                        "       abstract void primitiveOperation();",
                        "   }",
                        "   @TemplateMethodPattern.ConcreteClass",
                        "   class ConcreteClass extends AbstractClass {",
                        "       @TemplateMethodPattern.PrimitiveOperation",
                        "       void primitiveOperation() {",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("Operation in Concrete Class should override");
    }
}
