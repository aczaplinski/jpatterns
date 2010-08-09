package org.jpatterns.gof.builder;

import org.jpatterns.*;

import java.lang.annotation.*;

/**
 * @author Heinz Kabutz
 * @since 2010-08-09
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@DesignPattern(type = Type.CREATIONAL)
public @interface BuilderPattern {
}