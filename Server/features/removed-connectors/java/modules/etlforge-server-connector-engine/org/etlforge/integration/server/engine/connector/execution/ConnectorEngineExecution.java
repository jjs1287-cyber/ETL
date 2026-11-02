package org.etlforge.integration.server.engine.connector.execution;

import java.util.List;
import java.util.Map;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.server.engine.connector.autodesk.ConnectorEndpointAutoDesk;
import org.etlforge.integration.server.engine.connector.autodesk.ConnectorEngineAutoDesk;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerAccessRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerNotSupportedRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpoint;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.netsuite.ConnectorEngineNetSuite;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;

public class ConnectorEngineExecution {
   final ConnectorEndpoint connectorEndpoint;
   final ConnectorEngine engine;
   List<String> selectedObjects;

   public ConnectorEngineExecution(ConnectorEndpoint var1) {
      this.connectorEndpoint = var1;
      this.engine = var1.newConnectorEngine();
      this.selectedObjects = null;
   }

   public ConnectorEndpoint getConnectorEndpoint() {
      return this.connectorEndpoint;
   }

   public boolean testConnection() {
      return this.connectorEndpoint.testConnection();
   }

   public void initExecutionContext(List<String> var1) {
      this.selectedObjects = var1;
   }

   public ConnectorObjectList getConnectorObjectList(String var1) {
      return this.engine.getConnectorObjectList(this.selectedObjects, var1);
   }

   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1) {
      ConnectorXsdSchemaFiles var2 = this.engine.getConnectorSchemaFiles(var1, this.selectedObjects);
      if (var2 == null) {
         String var3 = this.connectorEndpoint.getEndpointTypeName();
         throw new ConnectorServerAccessRuntimeException("getConnectorSchemaFiles not implemented for the connector " + var3 + " !");
      } else {
         return var2;
      }
   }

   public void executeConnectorFileUtf8(String var1, String var2) {
      this.engine.executeConnectorFileUtf8(this.selectedObjects, var1, var2);
   }

   public String executeConnector(String var1) {
      if (this.engine instanceof ConnectorEngineNetSuite) {
         ConnectorEngineNetSuite var2 = (ConnectorEngineNetSuite)this.engine;
         return var2.executeConnector(this.selectedObjects, var1);
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("Not implemented!");
      }
   }

   public Map<String, String> getSavedSearch(String var1) {
      if (this.engine instanceof ConnectorEngineNetSuite) {
         ConnectorEngineNetSuite var2 = (ConnectorEngineNetSuite)this.engine;
         return var2.getSavedSearch(var1);
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("Not implemented!");
      }
   }

   public NetSuiteDefinitionContents getNetSuiteDefinitionContents() {
      if (this.engine instanceof ConnectorEngineNetSuite) {
         ConnectorEngineNetSuite var1 = (ConnectorEngineNetSuite)this.engine;
         return var1.getNetSuiteDefinitionContents();
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("Not implemented!");
      }
   }

   public NetSuiteObjectCustomFields getCustomFields(boolean var1, String var2) {
      if (this.engine instanceof ConnectorEngineNetSuite) {
         ConnectorEngineNetSuite var3 = (ConnectorEngineNetSuite)this.engine;
         return var3.getCustomFields(var1, var2);
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("Not implemented!");
      }
   }

   public String getAutoDeskItemVersionId(String var1, String var2) {
      ConnectorEngineAutoDesk var3 = this.getConnectorEngineAutoDesk();
      return var3.getAutoDeskItemVersionId(var1, var2);
   }

   public String login() {
      ConnectorEndpointAutoDesk var1 = this.getConnectorEndpointAutoDesk();
      return var1.login();
   }

   public void logout() {
      ConnectorEndpointAutoDesk var1 = this.getConnectorEndpointAutoDesk();
      var1.logout();
   }

   private ConnectorEngineAutoDesk getConnectorEngineAutoDesk() {
      if (this.engine instanceof ConnectorEngineAutoDesk) {
         return (ConnectorEngineAutoDesk)this.engine;
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("Not implemented!");
      }
   }

   private ConnectorEndpointAutoDesk getConnectorEndpointAutoDesk() {
      if (this.connectorEndpoint instanceof ConnectorEndpointAutoDesk) {
         return (ConnectorEndpointAutoDesk)this.connectorEndpoint;
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("Not implemented!");
      }
   }
}
