package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.etlforge.integration.salesforce.bulkload.Mapping;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.lang.KongaStringUtils;

final class MapListToPropertiesTranslator {
   public static Properties toProperties(List<Mapping> var0) {
      Properties var1 = new Properties();

      for (Mapping var3 : var0) {
         String var4 = var3.source;
         if (var4.length() > 2) {
            var4 = KongaStringUtils.removeStartAndEnd(var4, "\"");
         }

         String var5 = ImportExportFormat.getFieldNameForExport(var3.target);
         var1.setProperty(var4, var5);
      }

      return var1;
   }

   public static List<Mapping> toMapList(Properties var0, HeaderMappingsModel var1) {
      ArrayList var2 = Lists.newArrayList();

      for (String var4 : var0.stringPropertyNames()) {
         String var5 = getFieldName(var0, var4);
         SObjectSchema.Field var6 = var1.getField(var5);
         if (var6 != null) {
            var2.add(new Mapping(var4, var6));
         }
      }

      MapListToPropertiesTranslator.QuoteStrategy var7 = new MapListToPropertiesTranslator.QuoteStrategy(var1);
      return var7.correctSourceNamesIfNecessary(var2);
   }

   private static String getFieldName(Properties var0, String var1) {
      String var2 = var0.getProperty(var1);
      return ImportExportFormat.translateImportedFieldName(var2);
   }

   private MapListToPropertiesTranslator() {
   }

   private static class QuoteStrategy {
      private final ImmutableSet<String> sourceFields;

      public QuoteStrategy(HeaderMappingsModel var1) {
         this.sourceFields = ImmutableSet.copyOf(var1.getSourceHeader());
      }

      public List<Mapping> correctSourceNamesIfNecessary(List<Mapping> var1) {
         ArrayList var2 = Lists.newArrayList();

         for (Mapping var4 : var1) {
            if (this.sourceFields.contains(var4.source)) {
               var2.add(var4);
            } else if (this.sourceFields.contains(this.inQuotes(var4.source))) {
               var2.add(new Mapping(this.inQuotes(var4.source), var4.target));
            }
         }

         return var2;
      }

      private String inQuotes(String var1) {
         return "\"" + var1 + "\"";
      }
   }
}
