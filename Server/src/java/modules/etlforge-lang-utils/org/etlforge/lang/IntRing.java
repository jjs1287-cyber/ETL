package org.etlforge.lang;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import org.etlforge.system.ArgCheck;

public final class IntRing implements Iterable<Integer> {
   public static final IntRing.Direction FORWARD = IntRing.Direction.FORWARD;
   public static final IntRing.Direction BACKWARD = IntRing.Direction.BACKWARD;
   private final int direction;
   private final int length;

   public static IntRing forward(int var0) {
      return new IntRing(FORWARD, var0);
   }

   public static IntRing backward(int var0) {
      return new IntRing(BACKWARD, var0);
   }

   public IntRing(IntRing.Direction var1, int var2) {
      Preconditions.checkNotNull(var1);
      this.direction = var1 == IntRing.Direction.FORWARD ? 1 : -1;
      this.length = ArgCheck.checkPositive(var2, "length");
   }

   public int length() {
      return this.length;
   }

   public int next(int var1) {
      Preconditions.checkArgument(var1 >= -1 && var1 < this.length, "start must be -1, or in the range [0, " + this.length + ")");
      if (var1 == -1) {
         return this.first();
      }

      int var2 = var1 + this.direction;
      if (var2 < 0) {
         var2 = this.length - 1;
      } else if (var2 >= this.length) {
         var2 = 0;
      }

      return var2;
   }

   public int first() {
      return this.direction == 1 ? 0 : this.length - 1;
   }

   public int last() {
      return this.direction == 1 ? this.length - 1 : 0;
   }

   @Override
   public Iterator<Integer> iterator() {
      return new Iterator<Integer>() {
         private boolean started;
         private int value;

         @Override
         public boolean hasNext() {
            return true;
         }

         public Integer next() {
            if (this.started) {
               this.value = IntRing.this.next(this.value);
            } else {
               this.value = IntRing.this.first();
               this.started = true;
            }

            return this.value;
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public enum Direction {
      FORWARD,
      BACKWARD;

      public IntRing newRing(int var1) {
         return new IntRing(this, var1);
      }
   }
}
