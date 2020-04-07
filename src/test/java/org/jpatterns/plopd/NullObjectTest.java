package org.jpatterns.plopd;

import static junit.framework.Assert.assertEquals;

import org.jpatterns.core.ValidationErrorLevel;
import org.junit.Test;

public class NullObjectTest {
  @NullObjectPattern.AbstractObject(validationErrorLevel = ValidationErrorLevel.ERROR)
  private interface Policy {
    float calculateInterest();
  }

  @NullObjectPattern.NullObject(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class NullPolicy implements Policy {
    public float calculateInterest() {
      return 0.0f;
    }
  }

  @NullObjectPattern.RealObject(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class RealPolicy implements Policy {
    public float calculateInterest() {
      return 12.4f;
    }
  }

  @Test
  public void calculatingInterestWithNullObject() {
    Policy policy = new NullPolicy();
    assertEquals(0.0f, policy.calculateInterest());
  }

  @Test
  public void calculatingInterestWithRealObject() {
    Policy policy = new RealPolicy();
    assertEquals(12.4f, policy.calculateInterest(), 0.01f);
  }
}
