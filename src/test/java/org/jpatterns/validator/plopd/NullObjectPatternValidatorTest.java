package org.jpatterns.validator.plopd;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class NullObjectPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.plopd;",
                        "class Test {",
                        "   @NullObjectPattern.AbstractObject",
                        "   abstract class AbstractObject {",
                        "   }",
                        "   @NullObjectPattern.RealObject",
                        "   class RealObject extends AbstractObject {",
                        "   }",
                        "   @NullObjectPattern.NullObject",
                        "   class NullObject extends AbstractObject {",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNullObjectExtendingRealObject() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.plopd;",
                        "class Test {",
                        "   @NullObjectPattern.AbstractObject",
                        "   abstract class AbstractObject {",
                        "   }",
                        "   @NullObjectPattern.RealObject",
                        "   class RealObject extends AbstractObject {",
                        "   }",
                        "   @NullObjectPattern.NullObject",
                        "   class NullObject extends RealObject {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("NullObject should be a subtype of");
    }
}
