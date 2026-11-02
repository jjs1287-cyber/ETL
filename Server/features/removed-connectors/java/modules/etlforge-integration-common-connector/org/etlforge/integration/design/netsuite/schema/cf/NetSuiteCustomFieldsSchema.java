package org.etlforge.integration.design.netsuite.schema.cf;

import java.util.List;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldSchema;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedObject;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldTypeConverter;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaGlobalElement;
import org.etlforge.integration.design.netsuite.schema.ConnectorNetSuiteCustomFieldSchema;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteCustomFieldsSchema {
   private final NetSuiteDefinitionDesign def;
   private final NetSuiteCustomFieldTypeConverter typeConverter;
   private final String objectTypeName;
   final List<ConnectorCustomFieldSchema> customfields;
   final ConnectorNetSuiteCustomFieldSchema modifiedSchemas;
   final boolean isSearchRow;

   public NetSuiteCustomFieldsSchema(
      boolean var1, String var2, List<ConnectorCustomFieldSchema> var3, ConnectorNetSuiteCustomFieldSchema var4, NetSuiteDefinitionDesign var5
   ) {
      this.def = var5;
      this.typeConverter = var5.getNetSuiteConfig().getNSCustomFieldTypeConverter();
      this.isSearchRow = var1;
      this.objectTypeName = var2;
      this.customfields = var3;
      this.modifiedSchemas = var4;
   }

   public void buildSchemas() {
      String var1 = NetSuiteCustomFieldImplementedObject.getCustomFieldListComplexTypeNamespace(this.def.getNetSuiteDefinition());
      XsdGlobalElementName var2 = XsdGlobalElementName.newType(var1, "CustomFieldList");
      String var3 = this.getSchema(var2);
      NetSuiteCustomFieldsSchemaTransformer var4 = new NetSuiteCustomFieldsSchemaTransformer(this.objectTypeName, var3, this.customfields);
      String var5 = var4.getModifiedSchema();
      this.modifiedSchemas.add(var5);

      for (ConnectorCustomFieldSchema var7 : this.customfields) {
         XsdGlobalElementName var8 = var7.getNetsuiteType(var1);
         var3 = this.getSchema(var8);
         NetSuiteCustomFieldSchemaTransformer var9 = new NetSuiteCustomFieldSchemaTransformer(this.isSearchRow, this.objectTypeName, var3, var7);
         var5 = var9.getModifiedSchema();
         this.modifiedSchemas.add(var5);
      }
   }

   private String getSchema(XsdGlobalElementName var1) {
      NetSuiteSchemaGlobalElement var2 = this.def.getNetSuiteDefinition().getNetSuiteSchemaGlobalElementExists(var1);
      return var2.getXmlString();
   }
}
