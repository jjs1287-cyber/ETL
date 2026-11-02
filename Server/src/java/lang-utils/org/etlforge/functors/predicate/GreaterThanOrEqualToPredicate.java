  
                             
   
package org.etlforge.functors.predicate;

import org.etlforge.functors.predicate.ComparisonPredicate;

public class GreaterThanOrEqualToPredicate<T extends Comparable<T>>
extends ComparisonPredicate<T> {
    public GreaterThanOrEqualToPredicate(T t) {
        super(t, 1, true);
    }

    public static <E extends Comparable<E>> GreaterThanOrEqualToPredicate<E> newInstance(E e) {
        return new GreaterThanOrEqualToPredicate<E>(e);
    }
}

