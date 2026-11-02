package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

public final class NodeConstants {
   public static final String UNKNOWN = "(Unknown)";
   public static final String AUTO = "(Auto)";
   public static final String USER_DEFINED_TYPE = "(UserDefinedType)";

   public static void replace(Node var0, String var1, String var2) {
      int var3 = var0.getDisplay().indexOf(var1);
      int var4 = var3 + var1.length();
      String var5 = var0.getDisplay().substring(0, var3) + var2 + var0.getDisplay().substring(var4);
      var0.setDisplay(var5);
   }

   private NodeConstants() {
   }
}
