package org.jpatterns.gof.creational;

import org.jpatterns.core.*;
import org.jpatterns.gof.structural.CompositePattern;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 97]:</b> Separate the construction of a complex object
 * from its representation so that the same construction process can create
 * different representations.
 *
 * @author Heinz Kabutz
 * @since 2010-08-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.CREATIONAL,
    level = Level.Component,
    purpose = "Separate the construction of a complex object from its " +
        "representation so that the same construction process can create " +
        "different representations",
    related = {AbstractFactoryPattern.class, CompositePattern.class})
public @interface BuilderPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Director {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Builder {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteBuilder {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Product {
    Class[] participants() default {};

    String comment() default "";
  }

}
