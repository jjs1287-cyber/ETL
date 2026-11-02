package org.etlforge.integration.data.entity.sfdc;

public enum SfdcDebugMode {
   NONE("None"),
   DEBUG_ONLY("DebugOnly"),
   DB("Db");

   private final String stringRep;

   SfdcDebugMode(String var3) {
      this.stringRep = var3;
   }

   @Override
   public String toString() {
      return this.stringRep;
   }
}
