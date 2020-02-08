package org.jpatterns.gof.structural;

import org.jpatterns.core.*;
import org.jpatterns.gof.behavioral.StrategyPattern;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 175]:</b> Attach additional responsibilities to an object
 * dynamically. Decorators provide a flexible alternative to subclassing for
 * extending functionality.
 *
 * <img alt="Decorator Structure" src="http://www.jpatterns.org/uml/gof/DecoratorStructure.gif">
 *
 * @author Heinz Kabutz
 * @since 2010-08-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.STRUCTURAL,
    level = Level.Component,
    purpose = "Attach additional responsibilities to an object dynamically. " +
        "Decorators provide a flexible alternative to subclassing for extending " +
        "functionality.",
    alsoKnown = {"Wrapper"},
    related = {AdapterPattern.class, CompositePattern.class,
        StrategyPattern.class})
public @interface DecoratorPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Component {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Decorator {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteComponent {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteDecorator {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }
}
