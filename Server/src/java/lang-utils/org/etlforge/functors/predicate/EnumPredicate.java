  
                             
   
                                        
                                    
                                     
   
package org.etlforge.functors.predicate;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.io.Serializable;
import java.util.EnumSet;

public abstract class EnumPredicate<E extends Enum<E>>
implements Predicate<E>,
Serializable {
    public static <T extends Enum<T>> Predicate<T> is(T t) {
        return new Is<T>(t);
    }

    public static <T extends Enum<T>> Predicate<T> isNot(T t) {
        return Predicates.not(EnumPredicate.is(t));
    }

    public static <T extends Enum<T>> Predicate<T> isIn(EnumSet<T> enumSet) {
        return new IsIn<T>(enumSet);
    }

    public static <T extends Enum<T>> Predicate<T> isNotIn(EnumSet<T> enumSet) {
        return Predicates.not(EnumPredicate.isIn(enumSet));
    }

    private static final class IsIn<T extends Enum<T>>
    extends EnumPredicate<T> {
        private final EnumSet<T> values;

        public IsIn(EnumSet<T> enumSet) {
            this.values = enumSet;
        }

        public boolean apply(T t) {
            return this.values.contains(t);
        }
    }

    private static final class Is<T extends Enum<T>>
    extends EnumPredicate<T> {
        private final T value;

        public Is(T t) {
            this.value = t;
        }

        public boolean apply(T t) {
            return t == this.value;
        }
    }
}

