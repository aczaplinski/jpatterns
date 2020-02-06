package org.jpatterns.gof.creational;

import org.jpatterns.core.*;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 127]:</b> Ensure a class only has one instance, and
 * provide a global point of access to it.
 *
 * <img alt="Singleton Structure" src="http://www.jpatterns.org/uml/gof/SingletonStructure.gif">
 *
 * @author Alex Gout
 * @since 2010-08-08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.CREATIONAL,
    level = Level.SingleClass,
    purpose = "Ensure a class only has one instance, and provide a global " +
        "point of access to it.",
    related = {AbstractFactoryPattern.class, BuilderPattern.class,
        PrototypePattern.class})
public @interface SingletonPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Singleton {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";

    Variation variation() default Variation.LAZY;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @Documented
  @interface SingletonMethod {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  enum Variation {
    LAZY, EAGER
  }
}
