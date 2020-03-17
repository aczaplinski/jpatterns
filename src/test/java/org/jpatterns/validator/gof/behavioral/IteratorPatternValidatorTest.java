package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class IteratorPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @IteratorPattern.Aggregate",
                        "   abstract static class Aggregate {",
                        "       static {",
                        "           int x = 1;",
                        "       }",
                        "       {",
                        "       }",
                        "       void zoo() {",
                        "       }",
                        "       Iterator iterator() {",
                        "           throw new RuntimeException();",
                        "       }",
                        "   }",
                        "   @IteratorPattern.ConcreteAggregate",
                        "   static class ConcreteAggregate extends Aggregate {",
                        "       @Override",
                        "       Iterator iterator() {",
                        "           return new ConcreteIterator();",
                        "       }",
                        "   }",
                        "   @IteratorPattern.Iterator",
                        "   abstract static class Iterator {",
                        "       Object getElement() {",
                        "           return null;",
                        "       }",
                        "   }",
                        "   @IteratorPattern.ConcreteIterator",
                        "   static class ConcreteIterator extends Iterator {",
                        "       @Override",
                        "       Object getElement() {",
                        "           throw new RuntimeException();",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNoMethodReturningIterator() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @IteratorPattern.Aggregate",
                        "   abstract static class Aggregate {",
                        "       static {",
                        "           int x = 1;",
                        "       }",
                        "       {",
                        "       }",
                        "       void zoo() {",
                        "       }",
                        "       Iterator iterator = new ConcreteIterator();",
                        "   }",
                        "   @IteratorPattern.ConcreteAggregate",
                        "   static class ConcreteAggregate extends Aggregate {",
                        "       Object voo(Iterator iter) {",
                        "           return new ConcreteIterator();",
                        "       }",
                        "   }",
                        "   @IteratorPattern.Iterator",
                        "   abstract static class Iterator {",
                        "       Object getElement() {",
                        "           return null;",
                        "       }",
                        "   }",
                        "   @IteratorPattern.ConcreteIterator",
                        "   static class ConcreteIterator extends Iterator {",
                        "       @Override",
                        "       Object getElement() {",
                        "           throw new RuntimeException();",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("Aggregate should contain");
        assertThat(compilation).hadWarningContaining("ConcreteAggregate should contain");
    }

    @Test
    public void testJavaUtilIterator() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "import java.util.Iterator;",
                        "class Test {",
                        "   @IteratorPattern.Aggregate",
                        "   abstract static class Aggregate {",
                        "       abstract Iterator iterator();",
                        "   }",
                        "   @IteratorPattern.ConcreteAggregate",
                        "   static class ConcreteAggregate extends Aggregate {",
                        "       @Override",
                        "       Iterator iterator() {",
                        "           return new ConcreteIterator();",
                        "       }",
                        "   }",
                        "   @IteratorPattern.ConcreteIterator",
                        "   static class ConcreteIterator implements Iterator<String> {",
                        "       @Override",
                        "       public String next() {",
                        "           throw new RuntimeException();",
                        "       }",
                        "       @Override",
                        "       public boolean hasNext() {",
                        "           return false;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testNotOverriddenIteratorMethod() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "import java.util.Iterator;",
                        "class Test {",
                        "   @IteratorPattern.Aggregate",
                        "   abstract static class Aggregate {",
                        "       abstract Iterator iterator();",
                        "   }",
                        "   @IteratorPattern.Aggregate",
                        "   static abstract class SpecializedAggregate extends Aggregate {",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
