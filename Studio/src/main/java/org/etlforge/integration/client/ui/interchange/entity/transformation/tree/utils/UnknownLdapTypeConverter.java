package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

final class UnknownLdapTypeConverter {
   public static void convert(Node var0) {
      check(var0);
   }

   private static void check(Node var0) {
      if (var0.isUndefinedDataType()) {
         NodeConstants.replace(var0, "(Unknown)", "(UserDefinedType)");
      }

      if (var0.hasChildren()) {
         for (Node var2 : var0.getChildren()) {
            check(var2);
         }
      }
   }

   private UnknownLdapTypeConverter() {
   }
}
