package org.etlforge.integration.design.netsuite.definition;

import java.io.Serializable;

public class NetSuiteDefinitionCommon implements Serializable {
   private final ConnectorNetSuiteNamespaces connectorNetsuiteNamespaces;
   private final NetSuiteDefinitionConnector definitionConnector;
   private NetSuiteWsdlVersion wsdlVersion = null;
   private String wsdlServerUrl = null;

   public NetSuiteWsdlVersion getNetSuiteWsdlVersion() {
      return this.wsdlVersion;
   }

   public NetSuiteDefinitionCommon() {
      this.connectorNetsuiteNamespaces = new ConnectorNetSuiteNamespaces();
      this.definitionConnector = new NetSuiteDefinitionConnector();
   }

   void putPreferedPrefix(String var1, String var2) {
      this.setNetSuiteWsdlVersion(var1);
      this.connectorNetsuiteNamespaces.putPreferedPrefix(var1, var2);
   }

   public ConnectorNetSuiteNamespaces getConnectorNetSuiteNamespaces() {
      return this.connectorNetsuiteNamespaces;
   }

   public NetSuiteDefinitionConnector getDefinitionConnector() {
      return this.definitionConnector;
   }

   public String getWsdlServerUrl() {
      return this.wsdlServerUrl;
   }

   public void setServiceUrl(String var1) {
      this.wsdlServerUrl = var1;
   }

   private void setNetSuiteWsdlVersion(String var1) {
      if (this.wsdlVersion == null) {
         this.wsdlVersion = NetSuiteWsdlVersionFactory.fromNamespace(var1);
      }
   }
}
