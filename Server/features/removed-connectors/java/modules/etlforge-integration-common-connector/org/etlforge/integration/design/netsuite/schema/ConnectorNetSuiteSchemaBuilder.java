package org.etlforge.integration.design.netsuite.schema;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObjects;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedBaseTypeModifier;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaGlobalElement;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaXsd;
import org.etlforge.integration.design.netsuite.schema.cf.NetSuiteCustomFieldObjectSchemaTransformer;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class ConnectorNetSuiteSchemaBuilder {
   private static String xmlUtf8Header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
   final Map<String, ConnectorNetSuiteSchemaGlobalELements> schemaData;
   final NetSuiteObjects recordObjects;
   final ConnectorNetSuiteCustomFieldSchema customfieldSchemaBuilder;
   final NetSuiteDefinition def;
   NetSuiteSchemaXsd xsd = null;
   StringBuilder sb = null;

   public ConnectorNetSuiteSchemaBuilder(
      Map<String, ConnectorNetSuiteSchemaGlobalELements> var1, NetSuiteObjects var2, ConnectorNetSuiteCustomFieldSchema var3, NetSuiteDefinition var4
   ) {
      this.schemaData = var1;
      this.recordObjects = var2;
      this.customfieldSchemaBuilder = var3;
      this.def = var4;
   }

   public Map<String, String> build() {
      HashMap var1 = Maps.newHashMap();

      for (Entry var3 : this.schemaData.entrySet()) {
         String var4 = (String)var3.getKey();
         ConnectorNetSuiteSchemaGlobalELements var5 = (ConnectorNetSuiteSchemaGlobalELements)var3.getValue();
         this.build(var4, var5);
         var1.put(this.xsd.getSchemaLocation(), this.sb.toString());
      }

      return var1;
   }

   void build(String var1, ConnectorNetSuiteSchemaGlobalELements var2) {
      this.sb = new StringBuilder();
      this.xsd = this.def.getNetSuiteSchemaXsdExists(var1);
      this.sb.append(xmlUtf8Header);
      XmlElementUtils.startElementTagAttributeMap(
         this.sb, this.xsd.getSchemaQName(), this.xsd.getSchemaAttributeMap(), this.xsd.getPrefixNamespace(var2.dependNamespaces)
      );
      this.buildGlobalELements(var2);
      this.customfieldSchemaBuilder.appendSchema(var1, this.sb);
      XmlElementUtils.endElementTag(this.sb, this.xsd.getSchemaQName());
   }

   private void buildGlobalELements(ConnectorNetSuiteSchemaGlobalELements var1) {
      for (String var3 : var1.dependNamespaces) {
         NetSuiteSchemaXsd var4 = this.def.getNetSuiteSchemaXsdExists(var3);
         importElement(this.sb, this.xsd.getXsdNamespacePrefix(), var3, var4.getSchemaLocation());
      }

      for (XsdGlobalElementName var11 : var1.globalElements) {
         boolean var12 = this.def.getDefinitionConnector().isReplacedBaseType(this.recordObjects.isSearchRow(), var11);
         if (var12) {
            NetSuiteDerivedBaseTypeModifier var5 = new NetSuiteDerivedBaseTypeModifier(
               this.def, var11, this.xsd.getXsdNamespacePrefix(), this.recordObjects.getRecordObjects(), this.recordObjects.getAlwaysOneOf()
            );
            String var6 = var5.elementXmlString();
            this.sb.append(var6);
         } else {
            NetSuiteObject var13 = this.recordObjects.asCustomObject();
            NetSuiteSchemaGlobalElement var14 = this.def.getNetSuiteSchemaGlobalElementExists(var11);
            if (this.isCustomObjectRecord(var11, var13)) {
               String var7 = this.recordObjects.isSearch() ? null : this.customfieldSchemaBuilder.getCustomObjectId();
               NetSuiteCustomFieldObjectSchemaTransformer var8 = new NetSuiteCustomFieldObjectSchemaTransformer(
                  var13.getObjectElementName(), var14.getXmlString(), var7
               );
               String var9 = var8.getModifiedSchema();
               this.sb.append(var9);
               this.sb.append('\n');
            } else if (NetSuiteSearchObjectName.isSearchRowBasic(var11.getLocalName())) {
               NetSuiteSearchRowBasicTransformer var15 = new NetSuiteSearchRowBasicTransformer(this.recordObjects);
               String var17 = var15.transform(var11.getLocalName(), var14.getXmlString());
               if (!Strings.isNullOrEmpty(var17)) {
                  this.sb.append(var17);
               }
            } else if (NetSuiteSearchObjectName.isSearchRow(var11.getLocalName())) {
               NetSuiteSearchRowTransformer var16 = new NetSuiteSearchRowTransformer(this.recordObjects);
               String var18 = var16.transform(var14.getXmlString());
               this.sb.append(var18);
            } else {
               this.sb.append(var14.getXmlString());
            }
         }

         this.sb.append('\n');
      }
   }

   private boolean isCustomObjectRecord(XsdGlobalElementName var1, NetSuiteObject var2) {
      return var2 != null && var2.isType(var1);
   }

   private static void importElement(StringBuilder var0, String var1, String var2, String var3) {
      var0.append("<");
      if (!Strings.isNullOrEmpty(var1)) {
         var0.append(var1).append(':');
      }

      var0.append("import namespace=\"");
      var0.append(var2);
      var0.append("\" schemaLocation=\"");
      var0.append(var3);
      var0.append("\"/>\n");
   }
}
