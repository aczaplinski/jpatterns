package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class MediatorPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @MediatorPattern.Mediator",
                        "   abstract static class Mediator {",
                        "       static {",
                        "           int x = 1;",
                        "       }",
                        "       {",
                        "       }",
                        "       void zoo() {",
                        "       }",
                        "   }",
                        "   @MediatorPattern.ConcreteMediator",
                        "   static class ConcreteMediator extends Mediator {",
                        "   }",
                        "   @MediatorPattern.Colleague",
                        "   abstract static class Colleague {",
                        "       protected Mediator mediator = new ConcreteMediator();",
                        "       Object voo() {",
                        "           return \"abc\";",
                        "       }",
                        "   }",
                        "   @MediatorPattern.ConcreteColleague",
                        "   static class ConcreteColleague extends Colleague {",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testConcreteMediatorNotConcrete() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @MediatorPattern.Mediator",
                        "   abstract static class Mediator {",
                        "       static {",
                        "           int x = 1;",
                        "       }",
                        "       {",
                        "       }",
                        "       void zoo() {",
                        "       }",
                        "   }",
                        "   @MediatorPattern.ConcreteMediator",
                        "   static abstract class ConcreteMediator extends Mediator {",
                        "   }",
                        "   @MediatorPattern.Colleague",
                        "   abstract static class Colleague {",
                        "       private Mediator mediator = null;",
                        "       Object voo() {",
                        "           return \"abc\";",
                        "       }",
                        "   }",
                        "   @MediatorPattern.ConcreteColleague",
                        "   static class ConcreteColleague extends Colleague {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("ConcreteMediator should not be");
    }

    @Test
    public void testConcreteColleagueWithNoMediatorReference() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @MediatorPattern.Mediator",
                        "   abstract static class Mediator {",
                        "       static {",
                        "           int x = 1;",
                        "       }",
                        "       {",
                        "       }",
                        "       void zoo() {",
                        "       }",
                        "   }",
                        "   @MediatorPattern.ConcreteMediator",
                        "   static class ConcreteMediator extends Mediator {",
                        "   }",
                        "   @MediatorPattern.Colleague",
                        "   abstract static class Colleague {",
                        "       Object voo() {",
                        "           return \"abc\";",
                        "       }",
                        "   }",
                        "   @MediatorPattern.ConcreteColleague",
                        "   static class ConcreteColleague extends Colleague {",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("ConcreteColleague should store");
    }
}
