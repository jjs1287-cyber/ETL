  
                             
   
package org.etlforge.functors.predicate;

import org.etlforge.functors.predicate.ComparisonPredicate;

public class LessThanOrEqualToPredicate<T extends Comparable<T>>
extends ComparisonPredicate<T> {
    public LessThanOrEqualToPredicate(T t) {
        super(t, -1, true);
    }

    public static <E extends Comparable<E>> LessThanOrEqualToPredicate<E> newInstance(E e) {
        return new LessThanOrEqualToPredicate<E>(e);
    }
}

