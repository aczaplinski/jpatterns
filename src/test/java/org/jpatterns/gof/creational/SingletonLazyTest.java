package org.jpatterns.gof.creational;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.jpatterns.core.ValidationErrorLevel;
import org.junit.Test;

public class SingletonLazyTest {
  @SingletonPattern.Singleton(variation = SingletonPattern.Variation.LAZY,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  public static class LazySingleton {
    private volatile static LazySingleton INSTANCE;

    private LazySingleton() {
    }

    @SingletonPattern.SingletonMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    public static LazySingleton getInstance() {
      LazySingleton result = INSTANCE;
      if (result == null) {
        synchronized (LazySingleton.class) {
          result = INSTANCE;
          if (result == null) {
            INSTANCE = result = new LazySingleton();
          }
        }
      }
      return result;
    }
  }

  @Test
  public void testLaziness() {
    assertNull(LazySingleton.INSTANCE);
    assertNotNull(LazySingleton.getInstance());
    assertNotNull(LazySingleton.INSTANCE);
  }
}
