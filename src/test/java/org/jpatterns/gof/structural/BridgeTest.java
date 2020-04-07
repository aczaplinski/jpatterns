package org.jpatterns.gof.structural;

import org.jpatterns.core.ValidationErrorLevel;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Alex Gout
 * @since 2010-09-10
 */
public class BridgeTest {
  ListImplementor list1;
  ListImplementor list2;

  @BridgePattern.Implementor(participants = ListLayout.class,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  private interface ListImplementor {
    int getSize();

    String getItem(int _index);

    void addItem(String _item);
  }

  @BridgePattern.ConcreteImplementor(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class SomeList implements ListImplementor {
    private List<String> items = new ArrayList<>();

    public int getSize() {
      return items.size();
    }

    public String getItem(int _index) {
      return this.items.get(_index);
    }

    public void addItem(String _item) {
      this.items.add(_item);
    }
  }

  @BridgePattern.ConcreteImplementor(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class UppercaseList extends SomeList {

    public void addItem(String _item) {
      super.addItem(_item.toUpperCase());
    }
  }

  @BridgePattern.Abstraction(participants = ListImplementor.class,
    validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class ListLayout {
    protected ListImplementor implementor;

    public void setImplementor(ListImplementor implementor) {
      this.implementor = implementor;
    }

    public void printList() {
      for (int i = 0; i < implementor.getSize(); i++) {
        System.out.println(getItem(i));
      }
    }

    public String getItem(int _index) {
      return implementor.getItem(_index);
    }
  }

  @BridgePattern.RefinedAbstraction(validationErrorLevel = ValidationErrorLevel.ERROR)
  private static class NumberedListLayout extends ListLayout {

    @Override
    public String getItem(int _index) {
      return "item" + (_index + 1) + ":\t" + super.getItem(_index);
    }
  }

  @Before
  public void initializeLists() {

    list1 = new SomeList();
    list1.addItem("someList:item1");
    list1.addItem("someList:item2");
    list1.addItem("someList:item3");

    list2 = new UppercaseList();
    list2.addItem("uppercaseList:item1");
    list2.addItem("uppercaseList:item2");
    list2.addItem("uppercaseList:item3");
  }

  @Test
  public void SomeListBaseLayout() {
    ListLayout listLayout = new ListLayout();
    listLayout.setImplementor(list1);
    String printedFirstItem = listLayout.getItem(0);

    assertEquals("someList:item1", printedFirstItem);
  }

  @Test
  public void SomeListNumberedLayout() {
    ListLayout listLayout = new NumberedListLayout();
    listLayout.setImplementor(list1);
    String printedFirstItem = listLayout.getItem(0);

    assertEquals("item1:\tsomeList:item1", printedFirstItem);
  }

  @Test
  public void fullTest() {
    ListLayout listLayout = new ListLayout();
    listLayout.setImplementor(list1);
    listLayout.printList();

    listLayout.setImplementor(list2);
    listLayout.printList();

    listLayout = new NumberedListLayout();
    listLayout.setImplementor(list1);
    listLayout.printList();

    listLayout.setImplementor(list2);
    listLayout.printList();
  }

}
