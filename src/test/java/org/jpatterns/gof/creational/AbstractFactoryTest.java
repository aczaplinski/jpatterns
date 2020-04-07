package org.jpatterns.gof.creational;

import org.jpatterns.core.ValidationErrorLevel;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Alex Gout
 * @since 2010-09-06
 */
public class AbstractFactoryTest {
  private String configuredLocale;

  @AbstractFactoryPattern.AbstractProduct(participants = AbstractLocalizedAnimalFactory.class,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  private interface LocalizedAnimal {
    String talk();
  }

  @AbstractFactoryPattern.ConcreteProduct(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class DutchCat implements LocalizedAnimal {
    public String talk() {
      return "Miauw";
    }
  }

  @AbstractFactoryPattern.ConcreteProduct(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class EnglishCat implements LocalizedAnimal {
    public String talk() {
      return "Meow";
    }
  }

  @AbstractFactoryPattern.ConcreteProduct(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class DutchChicken implements LocalizedAnimal {
    public String talk() {
      return "Tok tok";
    }
  }

  @AbstractFactoryPattern.ConcreteProduct(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class EnglishChicken implements LocalizedAnimal {
    public String talk() {
      return "Cluck cluck";
    }
  }

  @AbstractFactoryPattern.AbstractFactory(participants = LocalizedAnimal.class,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  private interface AbstractLocalizedAnimalFactory {
    @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    LocalizedAnimal createCat();

    @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    LocalizedAnimal createChicken();
  }

  @AbstractFactoryPattern.ConcreteFactory(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class DutchAnimalFactory implements AbstractLocalizedAnimalFactory {
    @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    @Override
    public LocalizedAnimal createCat() {
      return new DutchCat();
    }

    @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    @Override
    public LocalizedAnimal createChicken() {
      return new DutchChicken();
    }
  }

  @AbstractFactoryPattern.ConcreteFactory(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class EnglishAnimalFactory implements AbstractLocalizedAnimalFactory {
    @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    @Override
    public LocalizedAnimal createCat() {
      return new EnglishCat();
    }

    @AbstractFactoryPattern.FactoryMethod(validationErrorLevel = ValidationErrorLevel.ERROR)
    @Override
    public LocalizedAnimal createChicken() {
      return new EnglishChicken();
    }
  }


  private AbstractLocalizedAnimalFactory getFactoryForConfiguredLocale() {
    if ("NL".equals(getConfigureLocale())) {
      return new DutchAnimalFactory();
    } else {
      return new EnglishAnimalFactory();
    }
  }

  private void configureLocale(String _locale) {
    this.configuredLocale = _locale;
  }

  private String getConfigureLocale() {
    return this.configuredLocale;
  }

  @Test
  public void testDutchAnimals() {
    configureLocale("NL");

    AbstractLocalizedAnimalFactory dutchFactory = getFactoryForConfiguredLocale();
    LocalizedAnimal dutchCat = dutchFactory.createCat();
    LocalizedAnimal dutchChicken = dutchFactory.createChicken();


    assertEquals("Miauw", dutchCat.talk());
    assertEquals("Tok tok", dutchChicken.talk());

  }

  @Test
  public void testEnglishAnimals() {
    configureLocale("EN");

    AbstractLocalizedAnimalFactory englishFactory = getFactoryForConfiguredLocale();
    LocalizedAnimal englishCat = englishFactory.createCat();
    LocalizedAnimal englishChicken = englishFactory.createChicken();

    assertEquals("Meow", englishCat.talk());
    assertEquals("Cluck cluck", englishChicken.talk());
  }

}
