package org.etlforge.integration.design.netsuite.adapter;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.design.netsuite.cf.ConnectorNetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedDefinition;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRequest;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRetrieverCustomObject;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRetrieverObject;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

public class NetSuiteDesignCustomFieldAdapter {
   private final NetSuiteDefinitionDesign designDef;
   private final NetSuiteCustomFieldsLookupIFactory factory;

   public NetSuiteDesignCustomFieldAdapter(NetSuiteDefinitionDesign var1, NetSuiteCustomFieldsLookupIFactory var2) {
      this.designDef = var1;
      this.factory = var2;
   }

   public static Set<String> getCustomFieldKey(NetSuiteDefinitionDesign var0, NetSuiteSelectedObject var1) {
      HashSet var2 = Sets.newHashSet();
      if (var1.isDelete()) {
         return var2;
      } else {
         String var3 = var1.getNetSuiteConnectorObject().getName();
         if (var1.isCustomObject()) {
            var2.add(var3);
            return var2;
         } else if (var1.isSearch()) {
            NetSuiteCustomFieldsRequestFactory var6 = new NetSuiteCustomFieldsRequestFactory(var0);
            NetSuiteCustomFieldsRequest var7 = var6.newCustomFieldRequestClient(var1.getNetSuiteConnectorObject(), true);
            return var7.getCustomizationTypes();
         } else {
            NetSuiteCustomFieldImplementedDefinition var4 = var0.getNetSuiteConfig().getNetSuiteCustomFieldDefinition();
            NetSuiteCustomFieldsRequest var5 = new NetSuiteCustomFieldsRequest(var4);
            var5.addObject(var3);
            var5.updateInfo();
            return var5.getCustomizationTypes();
         }
      }
   }

   public ConnectorNetSuiteCustomFields getSearchCustomFields(String var1) {
      boolean var2 = false;
      return this.getSearchCustomFields(var1, var2);
   }

   public ConnectorNetSuiteCustomFields getSearchCustomFields(String var1, boolean var2) {
      NetSuiteConnectorObject var3 = new NetSuiteConnectorObject(var1);
      if (var3.isCustomObject()) {
         NetSuiteCustomFieldsRetrieverCustomObject var7 = new NetSuiteCustomFieldsRetrieverCustomObject(this.factory, var3.getName());
         return var7.retrieve();
      } else {
         NetSuiteCustomFieldsRequestFactory var4 = new NetSuiteCustomFieldsRequestFactory(this.designDef);
         NetSuiteCustomFieldsRequest var5 = var2 ? var4.newCFRequestSearchBasicStandard(var3) : var4.newCustomFieldRequestClient(var3, false);
         NetSuiteCustomFieldsRetrieverObject var6 = new NetSuiteCustomFieldsRetrieverObject(this.factory, var5);
         return var6.retrieve();
      }
   }
}
