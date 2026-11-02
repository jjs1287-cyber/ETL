package org.etlforge.ui.util.find;

import com.google.common.primitives.Ints;
import org.etlforge.util.Interval;

public final class TextSearchMatch implements SearchTarget.Match, Interval {
   final int start;
   final int length;

   public TextSearchMatch(int var1, int var2) {
      this.start = var1;
      this.length = var2;
   }

   TextSearchMatch adjustLength(int var1) {
      return new TextSearchMatch(this.start, var1);
   }

   TextSearchMatch shiftStart(int var1) {
      return var1 == 0 ? this : new TextSearchMatch(this.start + var1, this.length);
   }

   @Override
   public int length() {
      return this.length;
   }

   @Override
   public int startsAt() {
      return this.start;
   }

   @Override
   public boolean contains(int var1) {
      return var1 >= this.start && var1 <= this.start + this.length;
   }

   @Override
   public int hashCode() {
      int var1 = this.start;
      return 31 * var1 + this.length;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof TextSearchMatch)) {
         return false;
      }

      TextSearchMatch var2 = (TextSearchMatch)var1;
      return this.start == var2.start && this.length == var2.length;
   }

   @Override
   public int compareLocation(SearchTarget.Match var1) {
      return Ints.compare(this.start, ((TextSearchMatch)var1).start);
   }

   @Override
   public String toString() {
      return "[start = " + this.startsAt() + ", length = " + this.length() + "]";
   }
}
