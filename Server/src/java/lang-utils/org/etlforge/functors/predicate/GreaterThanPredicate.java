  
                             
   
package org.etlforge.functors.predicate;

import org.etlforge.functors.predicate.ComparisonPredicate;

public class GreaterThanPredicate<T extends Comparable<T>>
extends ComparisonPredicate<T> {
    public GreaterThanPredicate(T t) {
        super(t, 1, false);
    }

    public static <E extends Comparable<E>> GreaterThanPredicate<E> newInstance(E e) {
        return new GreaterThanPredicate<E>(e);
    }
}

