package org.etlforge.integration.data.script;

import com.google.common.base.Objects;

final class FCN {
   private final int numberOfRequiredArgs;
   private final int numberOfOptionalArgs;
   private final boolean varargs;

   public static FCN fixed(int var0) {
      return new FCN(var0, 0, false);
   }

   public FCN(int var1, int var2, boolean var3) {
      this.numberOfRequiredArgs = var1;
      this.numberOfOptionalArgs = var2;
      this.varargs = var3;
   }

   public int getMinimumNumberOfArguments() {
      return this.numberOfRequiredArgs;
   }

   public boolean isFixedArgumentNumber() {
      return this.numberOfOptionalArgs == 0 && !this.varargs;
   }

   public FCN.ArgumentCheck checkFcnArguments(int var1) {
      boolean var2 = false;
      if (this.isFixedArgumentNumber()) {
         var2 = var1 == this.numberOfRequiredArgs;
      } else if (this.varargs) {
         var2 = var1 >= this.numberOfRequiredArgs;
      } else {
         assert this.numberOfOptionalArgs > 0;
         int var3 = this.getMaxNumberOfArguments();
         var2 = var1 >= this.numberOfRequiredArgs && var1 <= var3;
      }

      return var2 ? FCN.ArgumentCheck.pass : FCN.ArgumentCheck.fail;
   }

   private int getMaxNumberOfArguments() {
      assert !this.varargs;
      return this.numberOfRequiredArgs + this.numberOfOptionalArgs;
   }

   public String msg() {
      if (this.isFixedArgumentNumber()) {
         return "should be == " + this.numberOfRequiredArgs;
      }

      if (this.varargs) {
         return "should be >= " + this.numberOfRequiredArgs;
      }

      assert this.numberOfOptionalArgs > 0;
      int var1 = this.getMaxNumberOfArguments();
      return var1 - this.numberOfRequiredArgs == 1
         ? "should be " + this.numberOfRequiredArgs + " or " + this.getMaxNumberOfArguments()
         : "should be between " + this.numberOfRequiredArgs + " and " + this.getMaxNumberOfArguments();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.numberOfRequiredArgs, this.numberOfRequiredArgs, this.varargs);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof FCN)) {
         return false;
      }

      FCN var2 = (FCN)var1;
      return this.numberOfRequiredArgs == var2.numberOfRequiredArgs && this.numberOfOptionalArgs == var2.numberOfOptionalArgs && this.varargs == var2.varargs;
   }

   @Override
   public String toString() {
      return String.format("Required: %d, Optional: %d, Vararags: %s", this.numberOfRequiredArgs, this.numberOfOptionalArgs, this.varargs ? "true" : "false");
   }

   public enum ArgumentCheck {
      pass,
      fail;
   }
}
