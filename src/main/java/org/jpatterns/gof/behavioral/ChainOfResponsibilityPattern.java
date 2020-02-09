package org.jpatterns.gof.behavioral;

import org.jpatterns.core.*;
import org.jpatterns.gof.structural.CompositePattern;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 223]:</b> Avoid coupling the sender of a request to its
 * receiver by giving more than one object a chance to handle the request. Chain
 * the receiving objects and pass the request along the chain until an object
 * handles it.
 *
 * <img alt="Chain Of Responsibility Structure" src="http://www.jpatterns.org/uml/gof/ChainOfResponsibilityStructure.gif">
 *
 * @author Heinz Kabutz
 * @since 2010-07-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.BEHAVIORAL,
    level = Level.Component,
    purpose = "Avoid coupling the sender of a request to its receiver by giving " +
        "more than one object a chance to handle the request. Chain the " +
        "receiving objects and pass the request along the chain until an object " +
        "handles it.",
    related = {CompositePattern.class})
public @interface ChainOfResponsibilityPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Handler {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface ConcreteHandler {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";

    boolean defaultHandler() default false;
  }
}
