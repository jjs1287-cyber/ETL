package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

final class ImportExportFormat {
   public static String getFieldNameForExport(SObjectSchema.Field var0) {
      String var1 = var0.getName();
      return var1.replace(".", ":");
   }

   public static String translateImportedFieldName(String var0) {
      return var0 == null ? null : var0.replace(":", ".");
   }

   private ImportExportFormat() {
   }
}
