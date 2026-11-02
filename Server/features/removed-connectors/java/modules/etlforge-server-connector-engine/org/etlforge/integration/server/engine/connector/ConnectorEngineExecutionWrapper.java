package org.etlforge.integration.server.engine.connector;

import java.util.List;
import java.util.Map;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.server.engine.connector.endpoint.ConnectorEndpointTypeManager;
import org.etlforge.integration.server.engine.connector.execution.ConnectorEngineExecution;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpoint;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;

public class ConnectorEngineExecutionWrapper {
   final ConnectorEngineExecution execution;
   static final ConnectorEndpointTypeManager manager = new ConnectorEndpointTypeManager();

   public ConnectorEngineExecutionWrapper(ConnectorEndpointParameters var1) {
      ConnectorEndpoint var2 = this.newConnectorEndpoint(var1);
      this.execution = new ConnectorEngineExecution(var2);
   }

   private ConnectorEndpoint newConnectorEndpoint(ConnectorEndpointParameters var1) {
      ConnectorEndpoint var2 = manager.newConnectorEndpoint(var1.getEndpointTypeName());
      var2.setEndpointParameters(var1);
      return var2;
   }

   public boolean testConnection() {
      return this.execution.testConnection();
   }

   private void initExecutionContext(List<String> var1, boolean var2) {
      this.execution.initExecutionContext(var1);
   }

   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      this.initExecutionContext(var1, false);
      return this.execution.getConnectorObjectList(var2);
   }

   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      this.initExecutionContext(var2, false);
      return this.execution.getConnectorSchemaFiles(var1);
   }

   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      this.initExecutionContext(var1, true);
      this.execution.executeConnectorFileUtf8(var2, var3);
   }

   public Map<String, String> getSavedSearch(String var1) {
      return this.execution.getSavedSearch(var1);
   }

   public NetSuiteDefinitionContents getNetSuiteDefinitionContents() {
      return this.execution.getNetSuiteDefinitionContents();
   }

   public NetSuiteObjectCustomFields getCustomFields(boolean var1, String var2) {
      return this.execution.getCustomFields(var1, var2);
   }

   public String executeConnector(List<String> var1, String var2) {
      String var3 = ConnectorDefaultValueResolver.resolve(var2);
      this.initExecutionContext(var1, true);
      return this.execution.executeConnector(var3);
   }

   public String getAutoDeskItemVersionId(String var1, String var2) {
      return this.execution.getAutoDeskItemVersionId(var1, var2);
   }

   public String login() {
      return this.execution.login();
   }

   public void logout() {
      this.execution.logout();
   }
}
