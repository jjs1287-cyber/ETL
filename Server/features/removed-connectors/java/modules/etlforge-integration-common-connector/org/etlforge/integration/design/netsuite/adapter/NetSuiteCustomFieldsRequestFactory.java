package org.etlforge.integration.design.netsuite.adapter;

import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedDefinition;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRequest;
import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteDefinitionSearch;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSeachRow;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSeachRows;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearch;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteCustomFieldsRequestFactory {
   private final NetSuiteDefinitionDesign designDef;
   private static final String basicCategory = "basic";

   NetSuiteCustomFieldsRequestFactory(NetSuiteDefinitionDesign var1) {
      this.designDef = var1;
   }

   NetSuiteCustomFieldsRequest newNSCustomFieldsRequestServer(NetSuiteObjects var1) {
      if (!var1.isStandardObject()) {
         return null;
      }

      NetSuiteCustomFieldsRequest var2 = var1.isSearchRow() ? this.newCustomFieldSearchRow(var1) : this.newRecordObjects(var1);
      var2.updateInfo();
      return var2;
   }

   NetSuiteCustomFieldsRequest newCFRequestSearchBasicStandard(NetSuiteConnectorObject var1) {
      NetSuiteCustomFieldsRequest var2 = this.newCFRequestSearchBasicStandardImpl(var1);
      var2.updateInfo();
      return var2;
   }

   NetSuiteCustomFieldsRequest newCustomFieldRequestClient(NetSuiteConnectorObject var1, boolean var2) {
      NetSuiteCustomFieldsRequest var3 = this.newCustomFieldRequestClientImpl(var1, var2);
      var3.updateInfo();
      return var3;
   }

   private NetSuiteCustomFieldsRequest newRecordObjects(NetSuiteObjects var1) {
      NetSuiteCustomFieldImplementedDefinition var2 = this.designDef.getNetSuiteConfig().getNetSuiteCustomFieldDefinition();
      NetSuiteCustomFieldsRequest var3 = new NetSuiteCustomFieldsRequest(var2);

      for (NetSuiteObject var5 : var1.recordObjects) {
         var3.addObject(var5.getTypeLocalName());
      }

      return var3;
   }

   public NetSuiteCustomFieldsRequest newCustomFieldSearchRow(NetSuiteObjects var1) {
      NetSuiteCustomFieldImplementedDefinition var2 = this.designDef.getNetSuiteConfig().getNetSuiteCustomFieldDefinition();
      NetSuiteCustomFieldsRequest var3 = new NetSuiteCustomFieldsRequest(var2);

      for (NetSuiteObject var5 : var1.recordObjects) {
         this.retrieveCustomFieldSearchRow(var3, true, var5.getTypeLocalName(), false);
      }

      return var3;
   }

   private void retrieveCustomFieldSearchRow(NetSuiteCustomFieldsRequest var1, boolean var2, String var3, boolean var4) {
      NetSuiteDefinitionSearch var5 = this.designDef.getNetSuiteDefinitionSearch();
      NetSuiteSeachRows var6 = var5.getNSSeachRows();
      NetSuiteConfig var7 = this.designDef.getNetSuiteConfig();
      NetSuiteCustomSearchDefinition var8 = var7.getNetSuiteCustomSearchDefinition();
      NetSuiteSeachRow var9 = var6.getNSSeachRow(var3);
      if (var9 != null) {
         for (Entry var11 : var9.getRowBasics().entrySet()) {
            String var12 = var2 ? ((XsdGlobalElementName)var11.getValue()).getLocalName() : (String)var11.getKey();
            if (!var1.containsKey(var12)) {
               String var13 = NetSuiteSearchObjectName.getObjectNameFromSearchRowBasic(((XsdGlobalElementName)var11.getValue()).getLocalName());
               if (var8.isGenericCustomSearch(var13)) {
                  NetSuiteCustomSearch var14 = var8.get(var13);

                  for (Entry var16 : var14.getSubTypes().entrySet()) {
                     NetSuiteCustomSearchSubtype var17 = (NetSuiteCustomSearchSubtype)var16.getValue();
                     var1.addObject(var4, var12, var17.getSearchResponseObjectNames().get(0));
                  }
               } else {
                  var1.addObject(var4, var12, var13);
               }
            }
         }
      }
   }

   private NetSuiteCustomFieldsRequest newCustomFieldRequestClientImpl(NetSuiteConnectorObject var1, boolean var2) {
      NetSuiteCustomFieldImplementedDefinition var3 = this.designDef.getNetSuiteConfig().getNetSuiteCustomFieldDefinition();
      NetSuiteCustomFieldsRequest var4 = new NetSuiteCustomFieldsRequest(var3);
      String var5 = this.getBasicObjectName(var1);
      boolean var6 = !var1.isCustomObject();
      if (var5 == null) {
         NetSuiteObjectsCreator var7 = new NetSuiteObjectsCreator(
            this.designDef.getNetSuiteDefinition().getDefinitionConnector(), this.designDef.getNetSuiteConfig()
         );
         String var8 = NetSuiteObjectsCreator.getTypeSearchRowType(var6, var7.getSearchObjectName(var1));
         this.retrieveCustomFieldSearchRow(var4, false, var8, var2);
      } else {
         var4.addObject(var2, "basic", var5);
      }

      return var4;
   }

   private String getBasicObjectName(NetSuiteConnectorObject var1) {
      NetSuiteDefinitionSearch var2 = this.designDef.getNetSuiteDefinitionSearch();
      NetSuiteConfig var3 = this.designDef.getNetSuiteConfig();
      NetSuiteSeachRows var4 = var2.getNSSeachRows();
      NetSuiteCustomSearchDefinition var5 = var3.getNetSuiteCustomSearchDefinition();
      if (var1.isCustomObject()) {
         return null;
      }

      String var6 = var1.getName();
      if (var1.isCustomSearch()) {
         String var7 = var1.getKey();
         String var8 = getCustomFieldObjectName(var5, var7);
         if (var8 != null) {
            return var8;
         }
      } else if (!var4.hasAdvancedSearch(var6)) {
         return NetSuiteSearchObjectName.getSearchRowFromObjectName(var6);
      }

      return null;
   }

   private NetSuiteCustomFieldsRequest newCFRequestSearchBasicStandardImpl(NetSuiteConnectorObject var1) {
      NetSuiteCustomFieldImplementedDefinition var2 = this.designDef.getNetSuiteConfig().getNetSuiteCustomFieldDefinition();
      NetSuiteCustomFieldsRequest var3 = new NetSuiteCustomFieldsRequest(var2);
      String var4 = this.getCFRequestSearchBasicNameStandard(var1);
      if (var4 != null) {
         var3.addObject(false, "basic", var4);
      }

      return var3;
   }

   private String getCFRequestSearchBasicNameStandard(NetSuiteConnectorObject var1) {
      if (var1.isCustomObject()) {
         return var1.getName();
      }

      NetSuiteConfig var2 = this.designDef.getNetSuiteConfig();
      NetSuiteCustomSearchDefinition var3 = var2.getNetSuiteCustomSearchDefinition();
      if (var1.isCustomSearch()) {
         String var4 = var1.getKey();
         String var5 = getCustomFieldObjectName(var3, var4);
         if (var5 != null) {
            return var5;
         }
      }

      return var1.getName();
   }

   private static String getCustomFieldObjectName(NetSuiteCustomSearchDefinition var0, String var1) {
      if (var0.isGenericCustomSearch(var1)) {
         return null;
      } else {
         NetSuiteCustomSearch var2 = var0.getNetSuiteCustomSearch(var1);
         if (var2 != null) {
            NetSuiteCustomSearchSubtype var3 = var2.get(var1);
            return var3.getSearchResponseObjectNames().get(0);
         } else {
            return null;
         }
      }
   }
}
