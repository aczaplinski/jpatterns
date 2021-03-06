package org.jpatterns.gof.behavioral;

import org.jpatterns.core.*;
import org.jpatterns.gof.creational.PrototypePattern;
import org.jpatterns.gof.structural.CompositePattern;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 233]:</b> Encapsulate a request as an object, thereby
 * letting you parameterize clients with different requests, queue or log
 * requests, and support undoable operations.
 *
 * <img alt="Command Structure" src="http://www.jpatterns.org/uml/gof/CommandStructure.gif">
 *
 * @author Michael Hunger
 * @since 2010-08-08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(source = Source.GoF,
    type = Type.BEHAVIORAL,
    level = Level.SingleClass,
    alsoKnown = {"Action", "Transaction"},
    related = {CompositePattern.class, MementoPattern.class,
        PrototypePattern.class},
    purpose = "Encapsulate a request as an object, thereby letting you " +
        "parameterize clients with different requests, queue or log requests, " +
        "and support undoable operations.",
    urls = {"http://en.wikipedia.org/wiki/Command_pattern",
        "http://home.earthlink.net/~huston2/dp/command.html"},
    refactorings = {
        Refactoring.ExtractParameterObject,
        Refactoring.ExtractMethodObject}
)

public @interface CommandPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Command {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";

    boolean undoable() default false;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteCommand {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Invoker {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Receiver {
    Class[] participants() default {};

    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Client {
    Class[] participants() default {};

    String comment() default "";
  }
}
