package org.jpatterns.gof.creational;

import org.jpatterns.core.*;
import org.jpatterns.gof.behavioral.TemplateMethodPattern;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 107]:</b> Define an interface for creating an object, but
 * let subclasses decide which class to instantiate. Factory Method lets a class
 * defer instantiation to subclasses.
 *
 * This pattern refers to the GoF factory method, which differs greatly from the
 * static factory method commonly found in the refactoring literature.
 *
 * <img alt="Factory Method Structure" src="http://www.jpatterns.org/uml/gof/FactoryMethodStructure.gif">
 *
 * @author Heinz Kabutz
 * @since 2010-08-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.CREATIONAL,
    level = Level.SingleClass,
    purpose = "Define an interface for creating an object, but let subclasses " +
        "decide which class to instantiate. Factory Method lets a class defer " +
        "instantiation to subclasses.",
    related = {AbstractFactoryPattern.class, TemplateMethodPattern.class,
        PrototypePattern.class})
public @interface FactoryMethodPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Creator {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Product {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteCreator {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteProduct {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }
}
