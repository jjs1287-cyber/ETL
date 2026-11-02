package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObject;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectRow;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSearchField;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSearchWsdl;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteDefinitionQueryBuilderCreator {
   private final NetSuiteCustomSearchDefinition customSearchObjects;
   private final NetSuiteSearchWsdl searchWsdl;

   public NetSuiteDefinitionQueryBuilderCreator(NetSuiteDefinitionDesign var1) {
      this.customSearchObjects = var1.getNetSuiteConfig().getNetSuiteCustomSearchDefinition();
      this.searchWsdl = var1.getNetSuiteDefinitionSearch().getNetSuiteSearchWsdl();
   }

   public NetSuiteDefinitionQueryBuilder createInstance() {
      NetSuiteDefinitionQueryBuilder var1 = this.newNetSuiteDefinitionQuery();
      this.createSearchObject(var1);
      this.createSearchObjectAdvanced(var1);
      return var1;
   }

   private NetSuiteDefinitionQueryBuilder newNetSuiteDefinitionQuery() {
      NetSuiteSearchFieldDictionary var1 = new NetSuiteSearchFieldDictionary();
      var1.init(this.searchWsdl.asEnumTypeList());
      NetSuiteDefinitionQueryBuilder var2 = new NetSuiteDefinitionQueryBuilder(var1, this.searchWsdl.getNamespacePrefix());
      var2.netsuiteCustomSearchObjects = this.customSearchObjects;
      return var2;
   }

   void createSearchObject(NetSuiteDefinitionQueryBuilder var1) {
      NetSuiteSearchFieldDictionary var2 = var1.getNetSuiteSearchFields();

      for (Entry var4 : this.searchWsdl.getSearchObjects().entrySet()) {
         String var5 = ((XsdGlobalElementName)var4.getKey()).getLocalName();
         if (NetSuiteSearchObjectName.isSearchBasic(var5)) {
            String var11 = var5;
            NetSuiteSearchObject var12 = new NetSuiteSearchObject(
               ((XsdGlobalElementName)var4.getKey()).getNamespace(), ((XsdGlobalElementName)var4.getKey()).getLocalName()
            );

            for (Object __etlItem51 : (List)var4.getValue()) {
               NetSuiteSearchField var9 = (NetSuiteSearchField) __etlItem51;
               SearchField var10 = var2.getSearchField(var9.getFieldTypeLocalName());
               if (var10 != null) {
                  var12.addField(var9.getFieldName(), var10);
               }
            }

            var1.searchObjects.put(var11, var12);
         } else if (NetSuiteSearchObjectName.isSearchRowBasic(var5)) {
            NetSuiteSearchObjectRow var6 = new NetSuiteSearchObjectRow(
               ((XsdGlobalElementName)var4.getKey()).getNamespace(), ((XsdGlobalElementName)var4.getKey()).getLocalName()
            );
            var1.searchObjectRows.put(var5, var6);

            for (Object __etlItem65 : (List)var4.getValue()) {
               NetSuiteSearchField var8 = (NetSuiteSearchField) __etlItem65;
               var6.addField(var8.getFieldName());
            }
         }
      }
   }

   void createSearchObjectAdvanced(NetSuiteDefinitionQueryBuilder var1) {
      var1.advancedSearch = Maps.newHashMap();
      HashMap var2 = Maps.newHashMap();
      HashMap var3 = Maps.newHashMap();

      for (Entry var5 : this.searchWsdl.getSearchObjects().entrySet()) {
         String var6 = ((XsdGlobalElementName)var5.getKey()).getLocalName();
         if (NetSuiteSearchObjectName.isSearchAdvanced(var6)) {
            String var7 = NetSuiteSearchObjectName.getObjectNameFromSearchAdvanced(var6);
            List var8 = (List)var5.getValue();
            NetSuiteDefinitionObjectAdvanced var9 = new NetSuiteDefinitionObjectAdvanced(
               ((XsdGlobalElementName)var5.getKey()).getNamespace(), ((XsdGlobalElementName)var5.getKey()).getLocalName()
            );
            var1.advancedSearch.put(var7, var9);

            for (Object __etlItem87 : var8) {
               NetSuiteSearchField var11 = (NetSuiteSearchField) __etlItem87;
               if (var11.getFieldName().equals("criteria")) {
                  var3.put(var7, var11.getFieldTypeLocalName());
               } else {
                  var2.put(var7, var11.getFieldTypeLocalName());
               }
            }
         }
      }

      for (Entry var14 : var1.advancedSearch.entrySet()) {
         String var16 = (String)var3.get(var14.getKey());
         NetSuiteDefinitionObjectAdvanced var18 = (NetSuiteDefinitionObjectAdvanced)var14.getValue();

         for (NetSuiteSearchField var24 : this.searchWsdl.getSearchObjectIndexExists(var16)) {
            var18.add(var24.getFieldName(), var24.getFieldTypeLocalName());
         }
      }

      for (Entry var15 : var1.advancedSearch.entrySet()) {
         String var17 = (String)var2.get(var15.getKey());
         NetSuiteDefinitionObjectAdvanced var19 = (NetSuiteDefinitionObjectAdvanced)var15.getValue();

         for (NetSuiteSearchField var25 : this.searchWsdl.getSearchObjectIndexExists(var17)) {
            var19.addRow(var25.getFieldName(), var25.getFieldTypeLocalName());
         }
      }
   }
}
