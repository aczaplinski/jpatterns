package org.jpatterns.validator.gof.behavioral;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.jpatterns.validator.PatternValidatingAnnotationProcessor;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class ObserverPatternValidatorTest {

    @Test
    public void testCorrectUsage() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @ObserverPattern.Observer",
                        "   interface Observer {",
                        "   }",
                        "   @ObserverPattern.ConcreteObserver",
                        "   class ConcreteObserver implements Observer {",
                        "   }",
                        "   @ObserverPattern.Subject",
                        "   interface Subject {",
                        "   }",
                        "   @ObserverPattern.ConcreteSubject",
                        "   class ConcreteSubject implements Subject {",
                        "       private Observer observer = new ConcreteObserver();",
                        "   }",
                        "}"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    public void testConcreteClassesNotImplementingProperInterfaces() {
        Compilation compilation = javac()
                .withProcessors(new PatternValidatingAnnotationProcessor())
                .compile(JavaFileObjects.forSourceLines(
                        "Test",
                        "package org.jpatterns.gof.behavioral;",
                        "class Test {",
                        "   @ObserverPattern.Observer",
                        "   interface Observer {",
                        "   }",
                        "   @ObserverPattern.ConcreteObserver",
                        "   class ConcreteObserver {",
                        "   }",
                        "   @ObserverPattern.Subject",
                        "   interface Subject {",
                        "   }",
                        "   @ObserverPattern.ConcreteSubject",
                        "   class ConcreteSubject implements Comparable<Subject> {",
                        "       private Observer observer;",
                        "       @Override",
                        "       public int compareTo(Subject subject) {",
                        "           return 0;",
                        "       }",
                        "   }",
                        "}"));
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(2);
        assertThat(compilation).hadWarningContaining("ConcreteObserver should");
        assertThat(compilation).hadWarningContaining("ConcreteSubject should");
    }
}
