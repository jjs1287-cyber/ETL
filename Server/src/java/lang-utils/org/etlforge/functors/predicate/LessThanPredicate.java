  
                             
   
package org.etlforge.functors.predicate;

import org.etlforge.functors.predicate.ComparisonPredicate;

public class LessThanPredicate<T extends Comparable<T>>
extends ComparisonPredicate<T> {
    public LessThanPredicate(T t) {
        super(t, -1, false);
    }

    public static <E extends Comparable<E>> LessThanPredicate<E> newInstance(E e) {
        return new LessThanPredicate<E>(e);
    }
}

