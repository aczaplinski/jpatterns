package org.jpatterns.plopd;

import org.jpatterns.core.*;
import org.jpatterns.gof.behavioral.StrategyPattern;
import org.jpatterns.gof.creational.SingletonPattern;
import org.jpatterns.gof.structural.FlyweightPattern;

import java.lang.annotation.*;

/**
 * <b>Intent [PLoPD3, pg 5]:</b> A Null Object provides a surrogate for another
 * object that shares the same interface but does nothing.  Thus, the Null
 * Object encapsulates the implementation decisions of how to do nothing and
 * hides those details from its collaborators.
 *
 * @author Heinz Kabutz
 * @since 2010-09-01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD,
    ElementType.LOCAL_VARIABLE})
@Documented
@DesignPattern(
    source = Source.PLoPD3,
    type = Type.BEHAVIORAL,
    level = Level.Component,
    purpose = "A Null Object provides a surrogate for another object that " +
        "shares the same interface but does nothing.  Thus, the Null Object " +
        "encapsulates the implementation decisions of how to do nothing and " +
        "hides those details from its collaborators.",
    related = {FlyweightPattern.class, StrategyPattern.class,
        SingletonPattern.class})
public @interface NullObjectPattern {
  Class[] participants() default {};

  String comment() default "";

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface AbstractObject {
    Class[] participants() default {};

    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface RealObject {
    Class[] participants() default {};

    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @interface NullObject {
    Class[] participants() default {};

    String comment() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.FIELD,
      ElementType.LOCAL_VARIABLE})
  @Documented
  @interface Client {
    Class[] participants() default {};

    String comment() default "";
  }
}
