package org.jpatterns.gof.behavioral;

import org.jpatterns.core.*;
import org.jpatterns.gof.structural.*;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 243]:</b> Given a language, define a represention for its
 * grammar along with an interpreter that uses the representation to interpret
 * sentences in the language.
 *
 * @author Heinz Kabutz
 * @since 2010-08-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.BEHAVIORAL,
    level = Level.SingleClass,
    purpose = "Given a language, define a represention for its grammar along " +
      "with an interpreter that uses the representation to interpret sentences " +
      "in the language.",
    related = {CompositePattern.class, FlyweightPattern.class,
        IteratorPattern.class, VisitorPattern.class})
public @interface InterpreterPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Context {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface AbstractExpression {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface TerminalExpression {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface NonterminalExpression {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }
}
