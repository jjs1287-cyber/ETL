package org.etlforge.integration.design.netsuite.definition;

import java.io.Serializable;

public class NetSuiteWsdlVersion implements Serializable {
   private final int year;
   private final int version;

   public NetSuiteWsdlVersion(int var1, int var2) {
      this.year = var1;
      this.version = var2;
   }

   public String toWsdlVersion() {
      return this.toString();
   }

   public boolean usingScriptIdForCustomField() {
      return this.year > 2013 || this.version > 1;
   }

   @Override
   public String toString() {
      return String.format("%d_%d", this.year, this.version);
   }

   @Override
   public int hashCode() {
      return this.year * 10 + this.version;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof NetSuiteWsdlVersion)) {
         return false;
      }

      NetSuiteWsdlVersion var2 = (NetSuiteWsdlVersion)var1;
      return this.year == var2.year && this.version == var2.version;
   }
}
