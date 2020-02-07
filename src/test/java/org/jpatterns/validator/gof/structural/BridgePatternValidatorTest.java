package org.jpatterns.validator.gof.structural;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class BridgePatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.structural;",
                        "class Test {",
                        "   @BridgePattern.Implementor",
                        "   interface Implementor {",
                        "       void someMethod();",
                        "   }",
                        "   @BridgePattern.ConcreteImplementor",
                        "   class LazyConcreteImplementor implements Implementor {",
                        "       @Override",
                        "       public void someMethod() {",
                        "       }",
                        "   }",
                        "   @BridgePattern.ConcreteImplementor",
                        "   class HardWorkingConcreteImplementor implements Implementor {",
                        "       @Override",
                        "       public void someMethod() {",
                        "           for(int i = 0; i < 1_000_000_000; i++);",
                        "       }",
                        "   }",
                        "   @BridgePattern.Abstraction",
                        "   class Abstraction {",
                        "       protected Implementor implementor;",
                        "       Abstraction() {",
                        "           if(Math.random() < 0.5) {",
                        "               implementor = new LazyConcreteImplementor();",
                        "           } else {",
                        "               implementor = new HardWorkingConcreteImplementor();",
                        "           }",
                        "       }",
                        "       public void doTheWork() {",
                        "           implementor.someMethod();",
                        "       }",
                        "   }",
                        "   @BridgePattern.RefinedAbstraction",
                        "   class RefinedAbstraction extends Abstraction {",
                        "       public void doTheWork() {",
                        "           for(int i = 0; i < 2; i++) {",
                        "               if(Math.random() > 0.5) {",
                        "                   implementor.someMethod();",
                        "               }",
                        "           }",
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
                        "   @BridgePattern.Implementor",
                        "   class Implementor {",
                        "       public void someMethod() {",
                        "       }",
                        "   }",
                        "   @BridgePattern.ConcreteImplementor",
                        "   class LazyConcreteImplementor {",
                        "       public void someMethod() {",
                        "       }",
                        "   }",
                        "   @BridgePattern.ConcreteImplementor",
                        "   class HardWorkingConcreteImplementor extends Implementor {",
                        "       @Override",
                        "       public void someMethod() {",
                        "           for(int i = 0; i < 1_000_000_000; i++);",
                        "       }",
                        "   }",
                        "   @BridgePattern.Abstraction",
                        "   abstract class Abstraction {",
                        "       protected Implementor implementor;",
                        "       Abstraction() {",
                        "           implementor = new HardWorkingConcreteImplementor();",
                        "       }",
                        "       public abstract void doTheWork();",
                        "   }",
                        "   @BridgePattern.RefinedAbstraction",
                        "   class RefinedAbstraction extends Abstraction {",
                        "       public void doTheWork() {",
                        "           for(int i = 0; i < 2; i++) {",
                        "               if(Math.random() > 0.5) {",
                        "                   implementor.someMethod();",
                        "               }",
                        "           }",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(3);
        assertThat(compilation).hadWarningContaining("Abstraction should not be abstract");
        assertThat(compilation).hadWarningContaining("Implementor should be");
        assertThat(compilation).hadWarningContaining("ConcreteImplementor should be a subtype of");
    }
}
