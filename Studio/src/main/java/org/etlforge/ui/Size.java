package org.etlforge.ui;

import com.google.common.primitives.Ints;
import org.etlforge.system.ArgCheck;

public abstract class Size {
   protected final int size;

   public static Size.Columns columns(int var0) {
      return new Size.Columns(var0);
   }

   public static Size.Rows rows(int var0) {
      return new Size.Rows(var0);
   }

   protected Size(int var1) {
      this.size = ArgCheck.checkNotNegative(var1);
   }

   @Override
   public int hashCode() {
      return this.size;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 == null ? false : this.getClass().equals(var1.getClass()) && this.size == ((Size)var1).size;
      }
   }

   @Override
   public String toString() {
      return "" + this.size;
   }

   public static final class Columns extends Size implements Comparable<Size.Columns> {
      public static Size.Columns of(int var0) {
         return new Size.Columns(var0);
      }

      public Columns(int var1) {
         super(var1);
      }

      public int get() {
         return this.size;
      }

      public int compareTo(Size.Columns var1) {
         return Ints.compare(this.size, var1.size);
      }
   }

   public static final class Rows extends Size implements Comparable<Size.Rows> {
      public static Size.Rows of(int var0) {
         return new Size.Rows(var0);
      }

      public Rows(int var1) {
         super(var1);
      }

      public int get() {
         return this.size;
      }

      public int compareTo(Size.Rows var1) {
         return Ints.compare(this.size, var1.size);
      }
   }
}
