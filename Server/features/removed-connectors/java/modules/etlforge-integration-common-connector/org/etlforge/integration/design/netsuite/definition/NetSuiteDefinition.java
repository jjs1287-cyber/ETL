package org.etlforge.integration.design.netsuite.definition;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteDefinition implements Serializable {
   private final NetSuiteDefinitionCommon definitionCommon = new NetSuiteDefinitionCommon();
   private final Map<String, NetSuiteSchemaXsd> schemaXsds = Maps.newHashMap();
   private final NetSuiteDerivedTypes derivedTypes = new NetSuiteDerivedTypes();
   private final Map<XsdGlobalElementName, NetSuiteSchemaGlobalElement> elements = Maps.newHashMap();
   private final Map<String, XsdGlobalElementName> searchRowBasics = Maps.newHashMap();
   private final Map<String, NetSuiteOperationRootElements> operationRoots = Maps.newHashMap();

   public NetSuiteDefinitionCommon getNetSuiteDefinitionCommon() {
      return this.definitionCommon;
   }

   public ConnectorNetSuiteNamespaces getConnectorNetSuiteNamespaces() {
      return this.definitionCommon.getConnectorNetSuiteNamespaces();
   }

   public NetSuiteDefinitionConnector getDefinitionConnector() {
      return this.definitionCommon.getDefinitionConnector();
   }

   public NetSuiteOperationRootElements getNetSuiteOperationExists(String var1) {
      NetSuiteOperationRootElements var2 = this.operationRoots.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteDefinitionError("operation " + var1 + " not found");
      } else {
         return var2;
      }
   }

   public void putNetSuiteSchemaXsdWithPrefix(String var1, NetSuiteSchemaXsd var2) {
      String var3 = var2.getTargetNamespacePrefix();
      if (Strings.isNullOrEmpty(var3)) {
         throw new ConnectorNetSuiteDefinitionError("Prefered prefix is emmpty for targetNamespace " + var1);
      }

      this.definitionCommon.putPreferedPrefix(var1, var3);
      this.schemaXsds.put(var1, var2);
   }

   public void setNetSuiteSchemaXsd(String var1, NetSuiteSchemaXsd var2) {
      this.schemaXsds.put(var1, var2);
   }

   public NetSuiteSchemaXsd getNetSuiteSchemaXsd(String var1) {
      return this.schemaXsds.get(var1);
   }

   public String getSchemaLocationExists(String var1) {
      String var2 = this.getNetSuiteSchemaXsdExists(var1).getSchemaLocation();
      if (Strings.isNullOrEmpty(var2)) {
         throw new ConnectorNetSuiteDefinitionError("SchemaLocation for target Namespace " + var1 + " not found");
      } else {
         return var2;
      }
   }

   public NetSuiteSchemaXsd getNetSuiteSchemaXsdExists(String var1) {
      NetSuiteSchemaXsd var2 = this.getNetSuiteSchemaXsd(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteDefinitionError("Schema for target Namespace " + var1 + " not found");
      } else {
         return var2;
      }
   }

   public NetSuiteSchemaGlobalElement getNetSuiteSchemaGlobalElementExists(XsdGlobalElementName var1) {
      NetSuiteSchemaGlobalElement var2 = this.getGlobalElements().get(var1);
      if (var2 != null && !Strings.isNullOrEmpty(var2.getXmlString())) {
         return var2;
      } else {
         throw new ConnectorNetSuiteDefinitionError("No definition for " + var1);
      }
   }

   public NetSuiteDerivedTypes getDerivedTypes() {
      return this.derivedTypes;
   }

   public void putGlobalELement(XsdGlobalElementName var1, NetSuiteSchemaGlobalElement var2) {
      if (!var1.isElement() && NetSuiteSearchObjectName.isSearchRowBasic(var1.getLocalName())) {
         this.getSearchRowBasics().put(var1.getLocalName(), var1);
      }

      this.elements.put(var1, var2);
   }

   public NetSuiteSchemaGlobalElement getGlobalELementExists(XsdGlobalElementName var1) {
      if (this.elements.containsKey(var1)) {
         return this.elements.get(var1);
      } else {
         throw new ConnectorNetSuiteDefinitionError("Can not find type definition for " + var1);
      }
   }

   public Map<XsdGlobalElementName, NetSuiteSchemaGlobalElement> getGlobalElements() {
      return this.elements;
   }

   public NetSuiteWsdlVersion getNetSuiteWsdlVersion() {
      return this.definitionCommon.getNetSuiteWsdlVersion();
   }

   public String getWsdlServerUrl() {
      return this.definitionCommon.getWsdlServerUrl();
   }

   public void setServiceUrl(String var1) {
      this.definitionCommon.setServiceUrl(var1);
   }

   public Map<String, XsdGlobalElementName> getSearchRowBasics() {
      return this.searchRowBasics;
   }

   public Map<String, NetSuiteOperationRootElements> getOperationRoots() {
      return this.operationRoots;
   }
}
