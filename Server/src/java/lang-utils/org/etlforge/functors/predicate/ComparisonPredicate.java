  
                             
   
                                        
                                    
   
package org.etlforge.functors.predicate;

import com.google.common.base.Predicate;
import java.io.Serializable;

public abstract class ComparisonPredicate<T extends Comparable<T>>
implements Predicate<T>,
Serializable {
    private final T value;
    private final int sign;
    private final boolean acceptEqual;

    protected ComparisonPredicate(T t, int n, boolean bl) {
        this.value = t;
        this.sign = n;
        this.acceptEqual = bl;
    }

    public final boolean apply(T t) {
        int n = t.compareTo(this.value);
        return Integer.signum(n) == this.sign || n == 0 && this.acceptEqual;
    }
}

