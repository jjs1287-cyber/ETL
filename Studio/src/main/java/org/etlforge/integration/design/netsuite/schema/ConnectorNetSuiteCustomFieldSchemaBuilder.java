package org.etlforge.integration.design.netsuite.schema;

import java.util.List;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObjects;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldsSchema;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedObject;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRequest;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.schema.cf.NetSuiteCustomFieldsSchema;

public class ConnectorNetSuiteCustomFieldSchemaBuilder {
   private final ConnectorCustomFieldsSchema result;
   private final NetSuiteCustomFieldsRequest standardObjects;
   private final NetSuiteObjects objects;
   private final ConnectorNetSuiteCustomFieldSchema modifiedCFSchema;
   private final NetSuiteDefinitionDesign defAll;

   public ConnectorNetSuiteCustomFieldSchemaBuilder(
      NetSuiteDefinitionDesign var1, NetSuiteCustomFieldsRequest var2, ConnectorCustomFieldsSchema var3, NetSuiteObjects var4
   ) {
      this.standardObjects = var2;
      this.result = var3;
      this.objects = var4;
      this.modifiedCFSchema = new ConnectorNetSuiteCustomFieldSchema(
         NetSuiteCustomFieldImplementedObject.getCustomFieldListComplexTypeNamespace(var1.getNetSuiteDefinition())
      );
      this.defAll = var1;
   }

   public void build() {
      this.modifiedCFSchema.clear();
      if (this.objects.isStandardObject()) {
         for (String var2 : this.standardObjects.getKeySet()) {
            String var3 = var2;
            this.buildSchema(var3, var3);
         }
      } else {
         NetSuiteObject var4 = this.objects.asCustomObject();
         String var5 = var4.getObjectElementName();
         this.modifiedCFSchema.setCustomObjectId(this.result.getCustomObjectId());
         this.buildSchema(var5, this.result.getCustomObjectId());
      }
   }

   private void buildSchema(String var1, String var2) {
      List var3 = this.result.getCustomFields(var2);
      NetSuiteCustomFieldsSchema var4 = new NetSuiteCustomFieldsSchema(this.objects.isSearchRow(), var1, var3, this.modifiedCFSchema, this.defAll);
      var4.buildSchemas();
   }

   public ConnectorNetSuiteCustomFieldSchema getModifiedCustomFieldSchemas() {
      return this.modifiedCFSchema;
   }

   public String getCustomObjectId() {
      return this.modifiedCFSchema.getCustomObjectId();
   }
}
