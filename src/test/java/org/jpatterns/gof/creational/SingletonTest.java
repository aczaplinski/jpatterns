package org.jpatterns.gof.creational;

import static junit.framework.Assert.assertSame;

import org.jpatterns.core.ValidationErrorLevel;
import org.junit.Test;

/**
 * @author Alex Gout
 * @since 2010-08-08
 */
public class SingletonTest {
  @Test
  public void testSameInstance() {
    Singleton singleton = Singleton.getInstance();
    Singleton singleton2 = Singleton.getInstance();

    assertSame(singleton, singleton2);
  }

  @SingletonPattern.Singleton(variation = SingletonPattern.Variation.EAGER,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  public static class Singleton {
    private final static Singleton INSTANCE = new Singleton();

    private Singleton() {
    }

    @SingletonPattern.SingletonMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    public static Singleton getInstance() {
      return INSTANCE;
    }
  }
}
