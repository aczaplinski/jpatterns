package org.jpatterns.gof.creational;

import org.jpatterns.core.ValidationErrorLevel;
import org.junit.Test;

import java.util.*;

/**
 * @author Alex Gout
 * @since 2010-09-10
 */
public class FactoryMethodTest {

  @FactoryMethodPattern.Product(participants = NumberPrinter.class,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  private interface NumberGenerator {
    int next();
  }

  @FactoryMethodPattern.Creator(participants = NumberGenerator.class,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  private static abstract class NumberPrinter {

    public void printNumbers(int _upto) {
      Collection<Integer> result = new ArrayList<>();
      NumberGenerator numberGenerator = createNumberGenerator();
      int curNum = numberGenerator.next();
      while (curNum < _upto) {
        result.add(curNum);
        curNum = numberGenerator.next();
      }
      System.out.println(result);
    }

    protected abstract NumberGenerator createNumberGenerator();
  }

  @FactoryMethodPattern.ConcreteProduct(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class PrimeGenerator implements NumberGenerator {
    int number = 0;


    public int next() {
      while (!isPrime(number)) {
        number++;
      }
      return number++;
    }

    boolean isPrime(int n) {
      if (n % 2 == 0) return false;
      for (int i = 3; i * i <= n; i += 2) {
        if (n % i == 0)
          return false;
      }
      return true;
    }
  }

  @FactoryMethodPattern.ConcreteCreator(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class PrimePrinter extends NumberPrinter {

    @Override
    protected NumberGenerator createNumberGenerator() {
      return new PrimeGenerator();
    }
  }

  @FactoryMethodPattern.ConcreteProduct(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class FibonacciGenerator implements NumberGenerator {
    private int fibPos = 0;

    public int next() {
      return fibonacciNumber(fibPos++);
    }

    private int fibonacciNumber(int _pos) {
      if (_pos <= 1) return _pos;
      return fibonacciNumber(_pos - 1) + fibonacciNumber(_pos - 2);
    }
  }

  @FactoryMethodPattern.ConcreteCreator(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class FibonacciPrinter extends NumberPrinter {

    @Override
    protected NumberGenerator createNumberGenerator() {
      return new FibonacciGenerator();
    }
  }

  @Test
  public void testFibonacciPrinter() {
    NumberPrinter numPrinter = new FibonacciPrinter();
    numPrinter.printNumbers(1000);
  }

  @Test
  public void testPrimePrinter() {
    NumberPrinter numPrinter = new PrimePrinter();
    numPrinter.printNumbers(1000);
  }


}
