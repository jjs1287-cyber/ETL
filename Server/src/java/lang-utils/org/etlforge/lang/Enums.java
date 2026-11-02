  
                             
   
                                        
                                   
                                           
                                      
   
package org.etlforge.lang;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.EnumSet;

public final class Enums {
    public static <E extends Enum> Ordering<E> alphabeticalOrdering() {
        return new AlphabeticalOrder();
    }

    public static <E extends Enum<E>> ImmutableList<E> alphabeticalOrder(Class<E> clazz) {
        EnumSet<E> enumSet = EnumSet.allOf(clazz);
        return Enums.alphabeticalOrdering().immutableSortedCopy(enumSet);
    }

    public static <E extends Enum<E>> Function<E, String> toNameFunction() {
        return new ToName();
    }

    private Enums() {
    }

    private static final class ToName<E extends Enum>
    implements Function<E, String>,
    Serializable {
        private ToName() {
        }

        public String apply(E e) {
            return ((Enum)e).name();
        }

        public String toString() {
            return "to-name";
        }
    }

    private static final class AlphabeticalOrder<E extends Enum>
    extends Ordering<E>
    implements Serializable {
        private AlphabeticalOrder() {
        }

        public int compare(E e, E e2) {
            return ((Enum)e).name().compareTo(((Enum)e2).name());
        }

        public String toString() {
            return "alphabetical-order";
        }
    }
}

