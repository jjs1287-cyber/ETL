package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionObjectAdvanced;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionQueryBuilder;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchCustomFields;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchDesignException;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchFieldDictionary;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearch;

public class NetSuiteSearchAdvancedFactory {
   public static NetSuiteSearchRecord newSearchRecord(
      NetSuiteEndpoint var0, String var1, NetSuiteDefinitionQueryBuilderLookup var2, NetSuiteCustomFieldsQueryBuilderLookup var3
   ) throws NetSuiteSearchDesignException {
      NetSuiteConnectorObject var4 = new NetSuiteConnectorObject(var1);
      return newSearchRecord(var0, var4, var2, var3);
   }

   public static NetSuiteSearchRecord newSearchRecord(NetSuiteEndpoint var0, NetSuiteConnectorObject var1, NetSuiteDefinitionQueryBuilderLookup var2) throws NetSuiteSearchDesignException {
      return newSearchRecord(var0, var1, var2, null);
   }

   public static NetSuiteSearchRecord newSearchRecord(
      NetSuiteEndpoint var0, NetSuiteConnectorObject var1, NetSuiteDefinitionQueryBuilderLookup var2, NetSuiteCustomFieldsQueryBuilderLookup var3
   ) throws NetSuiteSearchDesignException {
      NetSuiteDefinitionQueryBuilder var4 = var2.lookupDefinition(var0);
      if (var3 == null) {
         return newNetSuiteSearchAdvanced(var1, var4, null);
      }

      NetSuiteSearchFieldDictionary var5 = var4.getNetSuiteSearchFields();
      NetSuiteSearchCustomFields var6 = var3.lookupCustomFields(var0, var1.getKey(), var5);
      return newNetSuiteSearchAdvanced(var1, var4, var6);
   }

   private static NetSuiteSearchRecord newNetSuiteSearchAdvanced(
      NetSuiteConnectorObject var0, NetSuiteDefinitionQueryBuilder var1, NetSuiteSearchCustomFields var2
   ) {
      NetSuiteSearchAdvancedFactory.CustomSearchInfo var3 = new NetSuiteSearchAdvancedFactory.CustomSearchInfo(var0, var1);
      NetSuiteSearchNamespacePrefixes var4 = var1.getNamespacePrefix();
      NetSuiteDefinitionObjectAdvanced var5 = var1.getNetSuiteDefinitionObjectAdvancedExists(var0);
      NetSuiteSearchAdvanced var6 = new NetSuiteSearchAdvanced(var5.getSearchObjectNamspaceName(), var5.getSearchObjectLocalName(), var5.getBasicObjectKey());

      for (String var9 : var5.getKeyList()) {
         boolean var10 = var5.isBasicObjectKey(var9);
         String var11 = var5.get(var9);
         NetSuiteSearchObject var12 = var1.getNetSuiteSearchBasicObjectExists(var11);
         NetSuiteSearchObjectCustomFields var7 = null;
         if (var3.onlyBasicForCustomFields) {
            if (var10) {
               var7 = getCustomFields(true, var9, var2);
            }
         } else {
            var7 = getCustomFields(false, var9, var2);
         }

         NetSuiteSearchObjectSpecialField var13 = null;
         if (var10) {
            if (var3.customSearchObject != null) {
               var13 = new NetSuiteSearchObjectSpecialField(var3.customSearchObject, null);
            } else if (var0.isCustomObject()) {
               if (var7 == null) {
                  throw new RuntimeException("Fatal bug (NULL POINTER) to get custom field for custom object" + var0.getName());
               }

               var13 = new NetSuiteSearchObjectSpecialField(null, var7.getCustomObject());
            }
         }

         NetSuiteSearchObjectAllFields var14 = new NetSuiteSearchObjectAllFields(var12, var13, var7);
         NetSuiteSearchBasic var15 = new NetSuiteSearchBasic(var14, var4, var13);
         var6.putNetSuiteSearchBasic(var9, var15);
      }

      for (String var18 : var5.getRowKeyList()) {
         String var19 = var5.getRow(var18);
         NetSuiteSearchObjectRow var20 = var1.getNetSuiteSearchBasicRowExists(var19);
         NetSuiteSearchObjectCustomFields var16 = null;
         if (!var3.onlyBasicForCustomFields) {
            var16 = getCustomFields(false, var18, var2);
         }

         NetSuiteSearchBasicRow var21 = new NetSuiteSearchBasicRow(var20, var16, var4);
         var6.putNetSuiteSearchBasicRow(var18, var21);
      }

      return new NetSuiteSearchRecord(var0, var6, var1);
   }

   private static NetSuiteSearchObjectCustomFields getCustomFields(boolean var0, String var1, NetSuiteSearchCustomFields var2) {
      if (var2 == null) {
         return null;
      } else {
         return var0 ? var2.getOnlyOne() : var2.get(var1);
      }
   }

   private static class CustomSearchInfo {
      private final NetSuiteCustomSearchObject customSearchObject;
      private final boolean onlyBasicForCustomFields;

      CustomSearchInfo(NetSuiteConnectorObject var1, NetSuiteDefinitionQueryBuilder var2) {
         String var3 = var1.getKey();
         NetSuiteCustomSearch var4 = var2.getNetSuiteCustomSearch(var1);
         this.customSearchObject = var4 == null ? null : new NetSuiteCustomSearchObject(var4, var3);
         if (var1.isCustomObject()) {
            this.onlyBasicForCustomFields = true;
         } else if (var4 == null) {
            this.onlyBasicForCustomFields = !var2.isAdvancedStandardObject(var3);
         } else {
            this.onlyBasicForCustomFields = !var2.isGenericCustomSearch(var3);
         }
      }
   }
}
