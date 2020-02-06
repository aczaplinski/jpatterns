package org.jpatterns.validator.gof.creational;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class PrototypePatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @PrototypePattern.Prototype",
                        "   abstract class Prototype {",
                        "   }",
                        "   @PrototypePattern.ConcretePrototype",
                        "   class ConcretePrototype extends Prototype {",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNoAbstractPrototype() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.creational;",
                        "class Test {",
                        "   @PrototypePattern.ConcretePrototype",
                        "   class Prototype {",
                        "   }",
                        "   @PrototypePattern.ConcretePrototype",
                        "   class ConcretePrototype extends Prototype {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("should be a subtype of");
    }
}
