package org.etlforge.integration.etlforgeconnector.netsuite;

public enum NetSuiteConnectorObjectCategory {
   StandardObject(""),
   CustomObject("(custom)"),
   ItemSearchObject("(item)"),
   TranSearchObject("(tran)");

   private final String prefix;

   NetSuiteConnectorObjectCategory(String var3) {
      this.prefix = var3;
   }

   public String getPrefix() {
      return this.prefix;
   }

   static NetSuiteConnectorObjectCategory fromKey(String var0) {
      String var1 = var0.toLowerCase();

      for (NetSuiteConnectorObjectCategory var5 : values()) {
         if (var5 != StandardObject && var1.startsWith(var5.prefix)) {
            return var5;
         }
      }

      return StandardObject;
   }

   public static String getName(String var0) {
      int var1 = var0.indexOf(41);
      return var1 < 0 ? var0 : var0.substring(var1 + 1);
   }

   private static String toKey(NetSuiteConnectorObjectCategory var0, String var1) {
      return var0.prefix + var1;
   }

   public static String toCustomObjectKey(String var0) {
      return toKey(CustomObject, var0);
   }
}
