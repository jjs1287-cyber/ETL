package org.etlforge.util;

public abstract class AbstractInterval implements Interval {
   @Override
   public final boolean contains(int var1) {
      int var2 = this.startsAt();
      return var1 >= var2 && var1 <= var2 + this.length();
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Interval)) {
         return false;
      }

      Interval var2 = (Interval)var1;
      return this.startsAt() == var2.startsAt() && this.length() == var2.length();
   }

   @Override
   public final int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.startsAt();
      return 31 * var1 + this.length();
   }

   @Override
   public final String toString() {
      return "Interval[start = " + this.startsAt() + ", length = " + this.length() + "]";
   }
}
