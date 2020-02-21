package org.jpatterns.gof.structural;

import org.jpatterns.core.*;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 139]:</b> Convert the interface of a class into another
 * interface clients expect. Adapter lets classes work together that couldn't
 * otherwise because of incompatible interfaces.
 *
 * The structure for an object adapter is:
 *
 * <img alt="Object Adapter Structure" src="http://www.jpatterns.org/uml/gof/ObjectAdapterStructure.gif">
 *
 * The structure for a class adapter is:
 *
 * <img alt="Class Adapter Structure" src="http://www.jpatterns.org/uml/gof/ClassAdapterStructure.gif">
 *
 * @author Heinz Kabutz
 * @since 2010-07-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@DesignPattern(type = Type.STRUCTURAL,
    level = Level.Component,
    purpose = "Convert the interface of a class into another interface clients " +
        "expect. Adapter lets classes work together that couldn't otherwise " +
        "because of incompatible interfaces.",
    alsoKnown = {"Wrapper"},
    related = {BridgePattern.class, DecoratorPattern.class, ProxyPattern.class})
public @interface AdapterPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Adapter {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";

    Variation value() default Variation.OBJECT;
  }

  /**
   * We would hardly ever use this annotation as the adaptee is
   * usually not aware that it is being adapted.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Adaptee {
    Class[] participants() default {};

    String comment() default "";
  }

  enum Variation {
    OBJECT,
    CLASS,
    /**
     * See http://www.javaspecialists.eu/archive/Issue108.html
     */
    DYNAMIC
  }
}
