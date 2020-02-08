package org.jpatterns.validator.gof.structural;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class FlyweightPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @FlyweightPattern.FlyweightFactory",
                        "   class FlyweightFactory {",
                        "       Flyweight namelessFlyweight = new ConcreteFlyweight();",
                        "       Flyweight getFlyweight(String name) {",
                        "           if(name == null) {",
                        "               return namelessFlyweight;",
                        "           } else {",
                        "               return new UnsharedConcreteFlyweight(name);",
                        "           }",
                        "       }",
                        "   }",
                        "   @FlyweightPattern.Flyweight",
                        "   abstract class Flyweight {",
                        "   }",
                        "   @FlyweightPattern.ConcreteFlyweight",
                        "   class ConcreteFlyweight extends Flyweight {",
                        "   }",
                        "   @FlyweightPattern.UnsharedConcreteFlyweight",
                        "   class UnsharedConcreteFlyweight extends ConcreteFlyweight {",
                        "       private String name;",
                        "       UnsharedConcreteFlyweight(String name) {",
                        "           this.name = name;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNoFactoryMethod() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @FlyweightPattern.FlyweightFactory",
                        "   class FlyweightFactory {",
                        "       Flyweight namelessFlyweight = new ConcreteFlyweight();",
                        "       void getFlyweight(String name) {",
                        "           if(name == null) {",
                        "               return;",
                        "           } else {",
                        "               return;",
                        "           }",
                        "       }",
                        "   }",
                        "   @FlyweightPattern.Flyweight",
                        "   abstract class Flyweight {",
                        "   }",
                        "   @FlyweightPattern.ConcreteFlyweight",
                        "   class ConcreteFlyweight extends Flyweight {",
                        "   }",
                        "   @FlyweightPattern.UnsharedConcreteFlyweight",
                        "   class UnsharedConcreteFlyweight extends ConcreteFlyweight {",
                        "       private String name;",
                        "       UnsharedConcreteFlyweight(String name) {",
                        "           this.name = name;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining(
                "FlyweightFactory should contain a method returning");
    }

    @Test
    public void testConcreteFlyweight() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @FlyweightPattern.FlyweightFactory",
                        "   class FlyweightFactory {",
                        "       Flyweight namelessFlyweight = new ConcreteFlyweight();",
                        "       Flyweight getFlyweight(String name) {",
                        "           if(name == null) {",
                        "               return namelessFlyweight;",
                        "           } else {",
                        "               return new UnsharedConcreteFlyweight(name);",
                        "           }",
                        "       }",
                        "   }",
                        "   @FlyweightPattern.Flyweight",
                        "   class Flyweight {",
                        "   }",
                        "   @FlyweightPattern.ConcreteFlyweight",
                        "   class ConcreteFlyweight extends Flyweight {",
                        "   }",
                        "   @FlyweightPattern.UnsharedConcreteFlyweight",
                        "   class UnsharedConcreteFlyweight extends ConcreteFlyweight {",
                        "       private String name;",
                        "       UnsharedConcreteFlyweight(String name) {",
                        "           this.name = name;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("Flyweight should be");
    }
}
