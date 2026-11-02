package org.etlforge.integration.data.entity.sfdc;

public final class ScriptModificationWarning {
   public static String forScript() {
      return createWarning("script");
   }

   public static String forMapping() {
      return createWarning("mapping");
   }

   private static String createWarning(String var0) {
      return "// This " + var0 + " is part of a Salesforce wizard.\n// Modifying it may cause the wizard to malfunction.\n";
   }

   private ScriptModificationWarning() {
   }
}
