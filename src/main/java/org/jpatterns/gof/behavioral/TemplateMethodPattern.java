package org.jpatterns.gof.behavioral;

import org.jpatterns.core.*;
import org.jpatterns.gof.creational.FactoryMethodPattern;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 325]:</b> Define the skeleton of an algorithm in an
 * operation, deferring some steps to subclasses. Template Method lets
 * subclasses redefine certain steps of an algorithm without changing the
 * algorithm's structure.
 *
 * <img alt="Template Method Structure" src="http://www.jpatterns.org/uml/gof/TemplateMethodStructure.gif">
 *
 * @author Heinz Kabutz
 * @since 2010-07-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@DesignPattern(type = Type.BEHAVIORAL,
    level = Level.SingleClass,
    purpose = "Define the skeleton of an algorithm in an operation, deferring " +
        "some steps to subclasses. Template Method lets subclasses redefine " +
        "certain steps of an algorithm without changing the algorithm's structure.",
    related = {FactoryMethodPattern.class, StrategyPattern.class})
public @interface TemplateMethodPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface AbstractClass {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteClass {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @Documented
  @interface TemplateMethod {
    Class[] participants() default {};

    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @Documented
  @interface PrimitiveOperation {
    Class[] participants() default {};

    String comment() default "";
  }
}
