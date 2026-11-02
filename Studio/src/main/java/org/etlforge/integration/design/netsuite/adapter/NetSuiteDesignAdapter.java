package org.etlforge.integration.design.netsuite.adapter;

import java.util.Map;
import org.etlforge.integration.design.netsuite.NetSuiteXsdSchemaFiles;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldsSchema;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldsSchemaFactory;
import org.etlforge.integration.design.netsuite.cf.ConnectorNetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldTypeConverter;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRequest;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRetriever;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.definition.NetSuiteOperationRootElements;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;
import org.etlforge.integration.design.netsuite.schema.ConnectorNetSuiteSchema;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchRowPropertyies;

public class NetSuiteDesignAdapter {
   private final NetSuiteDefinitionDesign designDef;
   private final NetSuiteDefinition def;
   private final NetSuiteCustomFieldsLookupIFactory factory;
   private final NetSuiteSelectedObject selectedObject;

   public NetSuiteDesignAdapter(NetSuiteDefinitionDesign var1, NetSuiteCustomFieldsLookupIFactory var2, NetSuiteSelectedObject var3) {
      this.designDef = var1;
      this.def = var1.getNetSuiteDefinition();
      this.factory = var2;
      this.selectedObject = var3;
   }

   private NetSuiteOperationRootElements getNetSuiteOperationRootElements() {
      String var1 = this.selectedObject.getNetSuiteOperation();
      return this.def.getNetSuiteOperationExists(var1);
   }

   public NetSuiteXsdSchemaFiles getConnectorSchemaFiles() {
      NetSuiteObjectsCreator var1 = new NetSuiteObjectsCreator(this.def.getDefinitionConnector(), this.designDef.getNetSuiteConfig());
      NetSuiteObjects var2 = var1.getNSConnectorObjects(this.selectedObject);
      ConnectorNetSuiteSchema var3;
      if (this.selectedObject.isDelete()) {
         var3 = new ConnectorNetSuiteSchema(this.designDef, null, null);
      } else {
         NetSuiteCustomFieldsRequestFactory var4 = new NetSuiteCustomFieldsRequestFactory(this.designDef);
         NetSuiteCustomFieldsRequest var5 = var4.newNSCustomFieldsRequestServer(var2);
         if (this.selectedObject.hasSearchRows()) {
            NetSuiteSearchRowPropertyies var6 = this.selectedObject.getNetSuiteSearchRowPropertyies();
            ConnectorCustomFieldsSchema var7 = var6.getConnectorCustomFieldsSchema();
            var3 = new ConnectorNetSuiteSchema(this.designDef, var5, var7);
         } else {
            String var15 = this.selectedObject.isCustomObject() ? this.selectedObject.getNetSuiteConnectorObject().getName() : null;
            NetSuiteCustomFieldsRetriever var17 = new NetSuiteCustomFieldsRetriever(this.factory, var15, var5);
            ConnectorNetSuiteCustomFields var8 = var17.retrieve();
            NetSuiteCustomFieldTypeConverter var9 = this.designDef.getNetSuiteConfig().getNSCustomFieldTypeConverter();
            boolean var10 = this.selectedObject.responseIsSearchRow();
            NetSuiteWsdlVersion var11 = this.designDef.getNetSuiteDefinition().getNetSuiteWsdlVersion();
            ConnectorCustomFieldsSchema var12 = ConnectorCustomFieldsSchemaFactory.convert(var8, var10, var9, var11);
            var3 = new ConnectorNetSuiteSchema(this.designDef, var5, var12);
         }
      }

      NetSuiteOperationRootElements var13 = this.getNetSuiteOperationRootElements();
      Map var14 = var3.generate(var13, var2);
      boolean var16 = this.selectedObject.isSearch();
      String var18 = var16 ? "" : this.def.getSchemaLocationExists(var13.getRequestRoot().getNamespace());
      String var19 = var16 ? "" : var13.getRequestRoot().toExpandName();
      String var20 = this.def.getSchemaLocationExists(var13.getResponseRoot().getNamespace());
      String var21 = var13.getResponseRoot().toExpandName();
      return new NetSuiteXsdSchemaFiles(var18, var19, var20, var21, var14);
   }
}
