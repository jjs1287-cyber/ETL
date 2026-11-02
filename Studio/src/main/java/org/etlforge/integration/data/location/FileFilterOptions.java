package org.etlforge.integration.data.location;

import com.google.common.base.Preconditions;

public final class FileFilterOptions {
   private final boolean ignoreCase;
   private final boolean regularExpression;

   public FileFilterOptions() {
      this(false, false);
   }

   public FileFilterOptions(boolean var1, boolean var2) {
      Preconditions.checkArgument(!var1 || !var2, "ignoreCase and regularExpression cannot be used at the same time");
      this.ignoreCase = var1;
      this.regularExpression = var2;
   }

   public boolean isIgnoreCase() {
      return this.ignoreCase;
   }

   public boolean isRegularExpression() {
      return this.regularExpression;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof FileFilterOptions)) {
         return false;
      }

      FileFilterOptions var2 = (FileFilterOptions)var1;
      return this.ignoreCase == var2.ignoreCase && this.regularExpression == var2.regularExpression;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * (this.ignoreCase ? 0 : 1);
      return 31 * var1 + (this.regularExpression ? 0 : 1);
   }

   @Override
   public String toString() {
      return "[ignore case: " + this.ignoreCase + ", regEx: " + this.regularExpression + "]";
   }
}
