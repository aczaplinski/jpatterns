package org.jpatterns.gof.structural;

import org.jpatterns.core.*;

import java.lang.annotation.*;

import static org.jpatterns.core.ValidationErrorLevel.WARNING;

/**
 * <b>Intent [GoF, pg 207]:</b> Provide a surrogate or placeholder for another
 * object to control access to it.
 *
 * <img alt="Proxy Structure" src="http://www.jpatterns.org/uml/gof/ProxyStructure.gif">
 *
 * @author Heinz Kabutz
 * @since 2010-07-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD,
    ElementType.LOCAL_VARIABLE})
@Documented
@DesignPattern(type = Type.STRUCTURAL,
    level = Level.Component,
    purpose = "Provide a surrogate or placeholder for another object to control " +
        "access to it.",
    alsoKnown = {"Surrogate"},
    related = {AdapterPattern.class, DecoratorPattern.class})
public @interface ProxyPattern {
  Class[] participants() default {};

  String comment() default "";

  Variation variation() default Variation.STATIC_MANUAL;

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Subject {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface RealSubject {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface Proxy {
    Class[] participants() default {};
    ValidationErrorLevel validationErrorLevel() default WARNING;
    String comment() default "";

    Variation variation() default Variation.STATIC_MANUAL;

    Type type() default Type.UNDEFINED;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.FIELD,
      ElementType.LOCAL_VARIABLE})
  @Documented
  @interface Client {
    Class[] participants() default {};

    String comment() default "";
  }

  enum Variation {
    STATIC_MANUAL, STATIC_GENERATED, DYNAMIC
  }

  enum Type {
    UNDEFINED, VIRTUAL, REMOTE, PROTECTION
  }
}
