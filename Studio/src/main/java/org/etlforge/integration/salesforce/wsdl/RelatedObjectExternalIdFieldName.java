package org.etlforge.integration.salesforce.wsdl;

import org.apache.commons.lang.StringUtils;

public final class RelatedObjectExternalIdFieldName {
   public static final String SEPARATOR = ".";

   public static String quaulifyName(String var0, SObjectSchema.Field var1) {
      return var0 + "." + var1.getName();
   }

   public static boolean isQualifiedWith(String var0, SObjectSchema.Field var1) {
      String var2 = var1.getName();
      return var2.startsWith(var0 + ".");
   }

   public static boolean isRelationshipField(SObjectSchema.Field var0) {
      String var1 = var0.getName();
      return var1.contains(".");
   }

   public static String displayName(SObjectSchema.Field var0) {
      String var1 = var0.getName();
      return StringUtils.substringAfterLast(var1, ".");
   }

   private RelatedObjectExternalIdFieldName() {
   }
}
