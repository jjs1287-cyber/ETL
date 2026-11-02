package org.etlforge.integration.server.netsuite.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedDefinition;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedObject;
import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteDerivedTypes;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNames;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedBaseTypeModifier;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypeDefaultHandler;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypeHandler;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypeSearchRowHandler;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypeSimpleHandler;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypeTransformer;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypes;
import org.etlforge.integration.design.netsuite.definition.NetSuiteRecordRefTypeTransformer;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaGlobalElement;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaXsd;
import org.etlforge.integration.design.netsuite.schema.NetSuiteSearchRowBasicLoopRemover;
import org.etlforge.integration.design.netsuite.schema.cf.NetSuiteCustomFieldObjectSchemaTransformer;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdl;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlCreator;

public class NetSuiteDefinitionFactory {
   private final NetSuiteDefinitionContents contents;
   private final NetSuiteConfig searchConfig;
   private NetSuiteDefinition instance = null;

   NetSuiteDefinitionFactory(NetSuiteDefinitionContents var1, NetSuiteConfig var2) {
      this.contents = var1;
      this.searchConfig = var2;
   }

   public static NetSuiteDefinition create(NetSuiteDefinitionContents var0, NetSuiteConfig var1) {
      NetSuiteDefinitionFactory var2 = new NetSuiteDefinitionFactory(var0, var1);
      return var2.createImpl();
   }

   NetSuiteDefinition createImpl() {
      NetSuiteWsdlCreator var1 = new NetSuiteWsdlCreator(this.contents);
      NetSuiteWsdl var2 = var1.create();
      Object var6 = null;
      NetSuiteDefinitionCompressor var3 = new NetSuiteDefinitionCompressor(var2);
      this.instance = var3.compress();
      NetSuiteSearchObjectListFactory var4 = new NetSuiteSearchObjectListFactory(var2.getDefinitions());
      Object var7 = null;
      Object var8 = null;
      this.transformRecordDerivedTypes();
      this.transformSearchRowDerivedTypes();
      this.transformRecordRefDerivedTypes();
      this.transformBaseRefBaseType();
      Set var5 = this.instance.getDefinitionConnector().getRecordDeriveType().getKeySet();
      this.instance.getDefinitionConnector().setSearchRecordTypeNamePattern(var4.getSearchObjectNamePattern(var5));
      this.modifiedCustomFieldObjectSchema();
      this.modifiedCustomFieldSearchRowBasicSchema();
      this.removeLoopOfSearchRowBasicSchema();
      this.modifiedRecordRefSchema();
      return this.instance;
   }

   private void removeLoopOfSearchRowBasicSchema() {
      for (Entry var2 : this.instance.getSearchRowBasics().entrySet()) {
         this.removeLoopOfSearchRowBasicSchema((XsdGlobalElementName)var2.getValue());
      }
   }

   private void removeLoopOfSearchRowBasicSchema(XsdGlobalElementName var1) {
      NetSuiteSchemaGlobalElement var2 = this.instance.getGlobalELementExists(var1);
      String var3 = var2.getXmlString();
      String var4 = NetSuiteSearchRowBasicLoopRemover.removeLoop(var3);
      NetSuiteSchemaGlobalElement var5 = new NetSuiteSchemaGlobalElement(var2.getDependsGlobalElements(), var4);
      this.instance.putGlobalELement(var1, var5);
   }

   private void modifiedRecordRefSchema() {
      ConnectorNetSuiteDerivedTypes var1 = this.instance.getDefinitionConnector().getRecordRefDeriveType();
      if (var1 != null) {
         ConnectorNetSuiteNamespaces var2 = this.instance.getConnectorNetSuiteNamespaces();
         XsdGlobalElementName var3 = var1.getBaseType();
         NetSuiteSchemaGlobalElement var4 = this.instance.getNetSuiteSchemaGlobalElementExists(var3);
         String var5 = var2.getCoreNamespacePrefix();
         NetSuiteRecordRefTypeTransformer var6 = new NetSuiteRecordRefTypeTransformer(var5, var4.getXmlString());
         HashSet var7 = Sets.newHashSet();
         var7.addAll(var4.getDependsGlobalElements());
         this.instance.putGlobalELement(var3, new NetSuiteSchemaGlobalElement(var7, var6.getRecordRef()));
         XsdGlobalElementName var8 = var1.getDerivedObjectExists("CustomizationRef");
         this.instance.putGlobalELement(var8, new NetSuiteSchemaGlobalElement(var4.getDependsGlobalElements(), var6.getCustomizationRef()));
         var7.add(var8);
         XsdGlobalElementName var9 = XsdGlobalElementName.newType(var3.getNamespace(), "RecordRef_JB");
         this.instance.putGlobalELement(var9, new NetSuiteSchemaGlobalElement(var4.getDependsGlobalElements(), var6.getRecordJBRef()));
      }
   }

   private void modifiedCustomFieldSearchRowBasicSchema() {
      for (Entry var2 : this.instance.getSearchRowBasics().entrySet()) {
         String var3 = NetSuiteSearchObjectName.getObjectNameFromSearchRowBasic((String)var2.getKey());
         if (this.searchConfig.hasCustomFieldImplementation(var3)) {
            this.modifiedCustomFieldObjectSchema((XsdGlobalElementName)var2.getValue());
         }
      }
   }

   private void modifiedCustomFieldObjectSchema() {
      NetSuiteCustomFieldImplementedDefinition var1 = this.searchConfig.getNetSuiteCustomFieldDefinition();

      for (String var3 : var1.getHasCustomFieldsRecordObjects()) {
         NetSuiteCustomFieldImplementedObject var4 = var1.getNetSuiteCustomFieldObject(var3);
         this.modifiedCustomFieldObjectSchema(var4);
      }
   }

   private void modifiedCustomFieldObjectSchema(NetSuiteCustomFieldImplementedObject var1) {
      ConnectorNetSuiteDerivedTypes var2 = this.instance.getDefinitionConnector().getRecordDeriveType();
      XsdGlobalElementName var3 = var2.getDerivedObjectExists(var1.getObjectTypeName());
      this.modifiedCustomFieldObjectSchema(var3);
      if (var1.hasColumnCustomField()) {
         XsdGlobalElementName var4 = XsdGlobalElementName.newType(var3.getNamespace(), var1.getObjectTypeColumnName());
         this.modifiedCustomFieldObjectSchema(var4);
      }
   }

   private void modifiedCustomFieldObjectSchema(XsdGlobalElementName var1) {
      NetSuiteSchemaGlobalElement var2 = this.instance.getGlobalELementExists(var1);
      NetSuiteCustomFieldObjectSchemaTransformer var3 = new NetSuiteCustomFieldObjectSchemaTransformer(var1.getLocalName(), var2.getXmlString());
      String var4 = var3.getModifiedSchema();
      NetSuiteSchemaGlobalElement var5 = new NetSuiteSchemaGlobalElement(var2.getDependsGlobalElements(), var4);
      this.instance.putGlobalELement(var1, var5);
   }

   private void transformDerivedTypeContents(XsdGlobalElementName var1, Set<XsdGlobalElementName> var2, NetSuiteDerivedTypeTransformer var3) {
      for (XsdGlobalElementName var5 : var2) {
         NetSuiteSchemaGlobalElement var6 = this.instance.getNetSuiteSchemaGlobalElementExists(var5);
         String var7 = var3.transform(var5, var6.getXmlString());
         NetSuiteSchemaGlobalElement var8 = new NetSuiteSchemaGlobalElement(var6.getDependsGlobalElements(), var7);
         var8.removeBaseTypeDepends(var1);
         this.instance.putGlobalELement(var5, var8);
      }
   }

   private void transformRecordDerivedTypes() {
      NetSuiteDerivedTypeSimpleHandler var1 = new NetSuiteDerivedTypeSimpleHandler();
      ConnectorNetSuiteDerivedTypes var2 = this.transform("Record", var1);
      this.instance.getDefinitionConnector().setRecordDeriveType(var2);
   }

   private void transformRecordRefDerivedTypes() {
      NetSuiteDerivedTypes var2 = this.instance.getDerivedTypes();
      if (var2.hasBaseType("RecordRef")) {
         NetSuiteDerivedTypeSimpleHandler var3 = new NetSuiteDerivedTypeSimpleHandler();
         ConnectorNetSuiteDerivedTypes var4 = this.transform("RecordRef", var3);
         this.instance.getDefinitionConnector().setRecordRefDeriveType(var4);
      }
   }

   private void transformSearchRowDerivedTypes() {
      NetSuiteDerivedTypeSearchRowHandler var1 = new NetSuiteDerivedTypeSearchRowHandler();
      ConnectorNetSuiteDerivedTypes var2 = this.transform("SearchRow", var1);
      this.instance.getDefinitionConnector().setSearchRowDeriveType(var2);
   }

   private ConnectorNetSuiteDerivedTypes transform(String var1, NetSuiteDerivedTypeDefaultHandler var2) {
      NetSuiteDerivedTypes var3 = this.instance.getDerivedTypes();
      ConnectorNetSuiteDerivedTypes var4 = new ConnectorNetSuiteDerivedTypes(var1, var3);
      XsdGlobalElementName var5 = var4.getBaseType();
      Set var6 = var3.getDeriveTypes(var5);
      NetSuiteDerivedTypeTransformer var7 = new NetSuiteDerivedTypeTransformer(var2);
      this.transformDerivedTypeContents(var5, var6, var7);
      var3.remove(var5);
      return var4;
   }

   private void transformBaseRefBaseType() {
      NetSuiteDerivedTypes var1 = this.instance.getDerivedTypes();
      XsdGlobalElementName var2 = var1.getBaseTypeExists("BaseRef");
      NetSuiteSchemaXsd var3 = this.instance.getNetSuiteSchemaXsdExists(var2.getNamespace());
      String var4 = var3.getXsdNamespacePrefix();
      Set var5 = var1.getDeriveTypes(var2);
      NetSuiteDerivedTypeHandler var6 = new NetSuiteDerivedTypeHandler(ConnectorNetSuiteNames.getBaseRefElements());
      NetSuiteDerivedTypeTransformer var7 = new NetSuiteDerivedTypeTransformer(var6);
      this.transformDerivedTypeContents(var2, var5, var7);
      ConnectorNetSuiteDerivedTypes var8 = this.instance.getDefinitionConnector().getRecordRefDeriveType();
      List var9 = asBaseRefConnectorObjectList(var5, var8);
      NetSuiteDerivedBaseTypeModifier var10 = new NetSuiteDerivedBaseTypeModifier(this.instance, var2, var4, var9, false);
      String var11 = var10.elementXmlString();
      NetSuiteSchemaGlobalElement var12 = new NetSuiteSchemaGlobalElement(var5, var11);
      this.instance.putGlobalELement(var2, var12);
      var1.remove(var2);
   }

   private static List<NetSuiteObject> asBaseRefConnectorObjectList(Set<XsdGlobalElementName> var0, ConnectorNetSuiteDerivedTypes var1) {
      HashSet var2 = Sets.newHashSet();
      var2.addAll(var0);
      ArrayList var3 = Lists.newArrayList();
      XsdGlobalElementName var4 = var1 == null ? null : var1.getBaseType();

      for (Object __etlItem208 : var2) {
         XsdGlobalElementName var6 = (XsdGlobalElementName) __etlItem208;
         if (var4 != null && var1 != null && var6.equals(var4)) {
            XsdGlobalElementName var7 = XsdGlobalElementName.newType(var4.getNamespace(), "RecordRef_JB");
            var3.add(new NetSuiteObject(var4.getLocalName(), var7));
            var0.add(var7);
            XsdGlobalElementName var8 = var1.getDerivedObjectExists("CustomizationRef");
            var3.add(new NetSuiteObject(var8));
         } else {
            var3.add(new NetSuiteObject(var6));
         }
      }

      return var3;
   }
}
