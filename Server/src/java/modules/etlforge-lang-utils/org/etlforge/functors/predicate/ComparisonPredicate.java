package org.etlforge.functors.predicate;

import com.google.common.base.Predicate;
import java.io.Serializable;

public abstract class ComparisonPredicate<T extends Comparable<T>> implements Predicate<T>, Serializable {
   private final T value;
   private final int sign;
   private final boolean acceptEqual;

   protected ComparisonPredicate(T var1, int var2, boolean var3) {
      this.value = (T)var1;
      this.sign = var2;
      this.acceptEqual = var3;
   }

   public final boolean apply(T var1) {
      int var2 = var1.compareTo(this.value);
      return Integer.signum(var2) == this.sign || var2 == 0 && this.acceptEqual;
   }
}
