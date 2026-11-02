package com.etlforge.integration.client.ui.sfdc.soql.test;

import com.google.common.base.Objects;
import java.io.Serializable;

final class ExistingLimit {
   private final String queryString;
   private final int startOffset;
   private final int endOffset;
   private final Integer value;

   public ExistingLimit(String var1, int var2, int var3) {
      this.queryString = var1;
      this.startOffset = var2;
      this.endOffset = var3;
      this.value = this.extractValue();
   }

   private Integer extractValue() {
      String var1 = this.queryString.substring(this.startOffset, this.endOffset);
      if (var1.equalsIgnoreCase("limit")) {
         return null;
      }

      String var2 = var1.substring("limit".length()).trim();
      return !var2.isEmpty() && !var2.equals("-") ? Integer.valueOf(var2) : null;
   }

   public String replaceIfNecessary(int var1) {
      if (this.value != null && this.value > var1) {
         String var2 = "LIMIT " + var1;
         String var3 = this.queryString.substring(0, this.startOffset) + var2;
         if (this.endOffset < this.queryString.length()) {
            var3 = var3 + this.queryString.substring(this.endOffset);
         }

         return var3;
      } else {
         return this.queryString;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.queryString, this.startOffset, this.endOffset);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ExistingLimit)) {
         return false;
      }

      ExistingLimit var2 = (ExistingLimit)var1;
      return this.queryString.equals(var2.queryString) && this.startOffset == var2.startOffset && this.endOffset == var2.endOffset;
   }

   @Override
   public String toString() {
      String var1 = "LIMIT ";
      if (this.value != null) {
         var1 = var1 + this.value;
      }

      return var1;
   }
}

