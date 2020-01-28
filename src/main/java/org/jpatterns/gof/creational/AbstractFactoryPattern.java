package org.jpatterns.gof.creational;

import org.jpatterns.core.*;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 87]:</b> Provide an interface for creating families of
 * related or dependent objects without specifying their concrete classes.
 *
 * <img alt="Abstract Factory Structure" src="http://www.jpatterns.org/uml/gof/AbstractFactoryStructure.gif">
 *
 * <img alt="Abstract Factory User View Structure" src="http://www.jpatterns.org/uml/gof/AbstractFactoryUserViewStructure.gif">
 *
 * @author Heinz Kabutz, Michael Hunger
 * @since 2010-08-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.CREATIONAL,
    level = Level.Component,
    purpose = "Provide an interface for creating families of related or " +
        "dependent objects without specifying their concrete classes.",
    alsoKnown = {"Kit", "Toolkit"},
    related = {FactoryMethodPattern.class, PrototypePattern.class,
        SingletonPattern.class})
public @interface AbstractFactoryPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface AbstractProduct {
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

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @Documented
  @interface FactoryMethod {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface AbstractFactory {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteFactory {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }
} 
