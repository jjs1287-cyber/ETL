package org.etlforge.integration.salesforce;

import org.etlforge.system.ArgCheck;

public enum SalesforceVersion {
   V_23("23.0"),
   V_24("24.0"),
   V_28("28.0"),
   V_29("29.0"), V_61("61.0");

   private final String id;
   public static final SalesforceVersion DEFAULT_VERSION = V_29;

   SalesforceVersion(String var3) {
      this.id = ArgCheck.checkNotEmpty(var3);
   }

   public String getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return "Salesforce API version " + this.id;
   }

   public String getWsdlName() {
      String var1 = this.id.substring(0, 2);
      return "etlforge.salesforce.partner." + var1 + ".wsdl";
   }

   public static SalesforceVersion fromId(String var0) {
      if ("23".equals(var0)) {
         return V_23;
      }

      for (SalesforceVersion var4 : values()) {
         if (var4.id.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Unknown version ID: " + var0);
   }
}
