package org.jpatterns.gof.behavioral;

import static org.junit.Assert.assertTrue;

import org.jpatterns.core.ValidationErrorLevel;
import org.junit.Test;

/**
 * @author Michael Hunger
 * @since 2010-07-14
 */
@CommandPattern.Invoker(participants = CommandTest.Command.class,
        validationErrorLevel = ValidationErrorLevel.NONE)
public class CommandTest {
  @Test
  public void executeCommand() {
    final CommandReceiver receiver = new CommandReceiver();
    final Command command = new TestCommand(receiver);
    command.execute();
    assertTrue(receiver.ran);
  }

  @CommandPattern.Command(participants = CommandReceiver.class,
          validationErrorLevel = ValidationErrorLevel.ERROR)
  interface Command {
    void execute();
  }

  @CommandPattern.ConcreteCommand(comment = "This is our TestCommand",
          validationErrorLevel = ValidationErrorLevel.ERROR)
  static
  class TestCommand implements Command {
    private CommandReceiver receiver;

    TestCommand(CommandReceiver receiver) {
      this.receiver = receiver;
    }

    public void execute() {
      receiver.receive();
    }
  }

  @CommandPattern.Receiver(participants = Command.class)
  private static class CommandReceiver {
    private boolean ran;

    void receive() {
      ran = true;
    }
  }
}
