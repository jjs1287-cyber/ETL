package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchMetaData;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchObjectFieldMetaData;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchObjectMetaData;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObject;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectRow;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteDefinitionCreatorOld {
   private final NetSuiteSearchMetaData searchMetaData;

   public NetSuiteDefinitionCreatorOld(NetSuiteSearchMetaData var1) {
      this.searchMetaData = var1;
   }

   public NetSuiteDefinitionQueryBuilder createInstance() {
      NetSuiteDefinitionQueryBuilder var1 = new NetSuiteDefinitionQueryBuilder(
         this.createNetSuiteSearchFields(), this.searchMetaData.getTargetNamespacePrefixes()
      );
      this.createSearchObject(var1);
      this.createSearchObjectAdvanced(var1);
      var1.netsuiteCustomSearchObjects = this.searchMetaData.getNetSuiteCustomSearchDefinition();
      return var1;
   }

   private NetSuiteSearchFieldDictionary createNetSuiteSearchFields() {
      NetSuiteSearchFieldDictionary var1 = new NetSuiteSearchFieldDictionary();
      var1.init(this.searchMetaData.getEnumTypeList());
      return var1;
   }

   void createSearchObject(NetSuiteDefinitionQueryBuilder var1) {
      NetSuiteSearchFieldDictionary var2 = var1.getNetSuiteSearchFields();

      for (Entry var4 : this.searchMetaData.getNetSuiteSearchObjects().entrySet()) {
         String var5 = (String)var4.getKey();
         if (var5.endsWith("SearchBasic")) {
            String var10 = var5;
            NetSuiteSearchObject var11 = this.convertToNetSuiteSearchObject(var2, (NetSuiteSearchObjectMetaData)var4.getValue());
            var1.searchObjects.put(var10, var11);
         } else if (var5.endsWith("SearchRowBasic")) {
            NetSuiteSearchObjectMetaData var6 = (NetSuiteSearchObjectMetaData)var4.getValue();
            NetSuiteSearchObjectRow var7 = new NetSuiteSearchObjectRow(var6.getSearchObjectNamspaceName(), var6.getSearchObjectLocalName());
            var1.searchObjectRows.put(var5, var7);

            for (NetSuiteSearchObjectFieldMetaData var9 : var6.getSearchFields()) {
               var7.addField(var9.getFieldName());
            }
         }
      }
   }

   NetSuiteSearchObject convertToNetSuiteSearchObject(NetSuiteSearchFieldDictionary var1, NetSuiteSearchObjectMetaData var2) {
      NetSuiteSearchObject var3 = new NetSuiteSearchObject(var2.getSearchObjectNamspaceName(), var2.getSearchObjectLocalName());
      this.setSearchObject(var1, var2.getSearchFields(), var3);
      return var3;
   }

   void setSearchObject(NetSuiteSearchFieldDictionary var1, List<NetSuiteSearchObjectFieldMetaData> var2, NetSuiteSearchObject var3) {
      for (NetSuiteSearchObjectFieldMetaData var5 : var2) {
         SearchField var6 = var1.getSearchField(var5.getFieldType());
         if (var6 != null) {
            var3.addField(var5.getFieldName(), var6);
         }
      }
   }

   void createSearchObjectAdvanced(NetSuiteDefinitionQueryBuilder var1) {
      var1.advancedSearch = Maps.newHashMap();
      HashMap var2 = Maps.newHashMap();
      HashMap var3 = Maps.newHashMap();

      for (Entry var5 : this.searchMetaData.getNetSuiteSearchObjects().entrySet()) {
         String var6 = (String)var5.getKey();
         if (var6.endsWith("SearchAdvanced")) {
            String var7 = NetSuiteSearchObjectName.getObjectNameFromSearchAdvanced(var6);
            NetSuiteSearchObjectMetaData var8 = (NetSuiteSearchObjectMetaData)var5.getValue();
            NetSuiteDefinitionObjectAdvanced var9 = new NetSuiteDefinitionObjectAdvanced(var8.getSearchObjectNamspaceName(), var8.getSearchObjectLocalName());
            var1.advancedSearch.put(var7, var9);

            for (NetSuiteSearchObjectFieldMetaData var11 : var8.getSearchFields()) {
               if (var11.getFieldName().equals("criteria")) {
                  var3.put(var7, var11.getFieldType());
               } else {
                  var2.put(var7, var11.getFieldType());
               }
            }
         }
      }

      for (Entry var14 : var1.advancedSearch.entrySet()) {
         NetSuiteDefinitionObjectAdvanced var16 = (NetSuiteDefinitionObjectAdvanced)var14.getValue();
         String var18 = (String)var3.get(var14.getKey());
         NetSuiteSearchObjectMetaData var20 = this.searchMetaData.getNetSuiteSearchObjects().get(var18);

         for (NetSuiteSearchObjectFieldMetaData var24 : var20.getSearchFields()) {
            var16.add(var24.getFieldName(), var24.getFieldType());
         }
      }

      for (Entry var15 : var1.advancedSearch.entrySet()) {
         NetSuiteDefinitionObjectAdvanced var17 = (NetSuiteDefinitionObjectAdvanced)var15.getValue();
         String var19 = (String)var2.get(var15.getKey());
         NetSuiteSearchObjectMetaData var21 = this.searchMetaData.getNetSuiteSearchObjects().get(var19);

         for (NetSuiteSearchObjectFieldMetaData var25 : var21.getSearchFields()) {
            var17.addRow(var25.getFieldName(), var25.getFieldType());
         }
      }
   }
}
