package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class CommandPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @CommandPattern.Command",
                        "   interface Command {",
                        "       void execute();",
                        "   }",
                        "   @CommandPattern.ConcreteCommand",
                        "   class ConcreteCommand implements Command {",
                        "       private Receiver receiver;",
                        "       ConcreteCommand(Receiver receiver){",
                        "           this.receiver = receiver;",
                        "       }",
                        "       @Override",
                        "       public void execute() {",
                        "           receiver.performAction();",
                        "       }",
                        "   }",
                        "   @CommandPattern.Receiver",
                        "   class Receiver {",
                        "       public void performAction() {",
                        "       }",
                        "   }",
                        "   @CommandPattern.Invoker",
                        "   class Invoker {",
                        "       private Command command;",
                        "       Invoker(Command command) {",
                        "           this.command = command;",
                        "       }",
                        "       public void onClick() {",
                        "           command.execute();",
                        "       }",
                        "   }",
                        "   @CommandPattern.Client",
                        "   class Client {",
                        "       void setup() {",
                        "           new Invoker(new ConcreteCommand(new Receiver()));",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testCommandNotAbstract() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @CommandPattern.Command",
                        "   class Command {",
                        "       void execute() {",
                        "       }",
                        "   }",
                        "   @CommandPattern.ConcreteCommand",
                        "   class ConcreteCommand extends Command {",
                        "       private Receiver receiver;",
                        "       ConcreteCommand(Receiver receiver){",
                        "           this.receiver = receiver;",
                        "       }",
                        "       @Override",
                        "       public void execute() {",
                        "           receiver.performAction();",
                        "       }",
                        "   }",
                        "   @CommandPattern.Receiver",
                        "   class Receiver {",
                        "       public void performAction() {",
                        "       }",
                        "   }",
                        "   @CommandPattern.Invoker",
                        "   class Invoker {",
                        "       private Command command;",
                        "       Invoker(Command command) {",
                        "           this.command = command;",
                        "       }",
                        "       public void onClick() {",
                        "           command.execute();",
                        "       }",
                        "   }",
                        "   @CommandPattern.Client",
                        "   class Client {",
                        "       void setup() {",
                        "           new Invoker(new ConcreteCommand(new Receiver()));",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining(
                "Command should be");
    }
}
