package org.jpatterns.validator;

import org.jpatterns.gof.behavioral.IteratorPattern;
import org.jpatterns.gof.structural.DecoratorPattern;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** This class contains a mapping from Java standard library classes
 *  to their JPatterns annotations. Another solution could be to create
 *  a special flavour of JPatterns-annotated JDK. I believe the hardcoding
 *  approach is much simpler to use by JPatterns clients. */
class HardcodedAnnotations {
    static final Map<String, Set<Class<? extends Annotation>>> HARDCODED_ANNOTATIONS =
            Stream.<Map.Entry<Class<?>, Set<Class<? extends Annotation>>>>of(
                    Map.entry(Iterator.class, Set.of(IteratorPattern.Iterator.class)),
                    Map.entry(ListIterator.class, Set.of(IteratorPattern.Iterator.class)),
                    Map.entry(Spliterator.class, Set.of(IteratorPattern.Iterator.class)),
                    Map.entry(Iterable.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(Collection.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(AbstractCollection.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(List.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(AbstractList.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(AbstractSequentialList.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(Set.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(AbstractSet.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(SortedSet.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(NavigableSet.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(Map.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(AbstractMap.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(SortedMap.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(NavigableMap.class, Set.of(IteratorPattern.Aggregate.class)),
                    Map.entry(InputStream.class, Set.of(DecoratorPattern.Component.class)),
                    Map.entry(FilterInputStream.class, Set.of(DecoratorPattern.ConcreteDecorator.class)),
                    Map.entry(OutputStream.class, Set.of(DecoratorPattern.Component.class)),
                    Map.entry(FilterOutputStream.class, Set.of(DecoratorPattern.ConcreteDecorator.class))
            ).collect(Collectors.toMap(
                    entry -> entry.getKey().getName(),
                    Map.Entry::getValue
            ));

    private HardcodedAnnotations() { }
}
