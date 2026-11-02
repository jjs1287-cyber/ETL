package org.etlforge.integration.server.engine.connector.autodesk;

import java.util.List;
import org.etlforge.integration.server.engine.connector.autodesk.adapter.ConnectorEndpointAutoDeskAdapter;
import org.etlforge.integration.server.engine.connector.autodesk.adapter.ConnectorEngineAutoDeskAdapter;
import org.etlforge.integration.server.engine.connector.autodesk.adapter.ConnectorEngineAutoDeskBridge;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;

public class ConnectorEngineAutoDesk implements ConnectorEngine {
   private final ConnectorEngineAutoDeskAdapter adapter;

   ConnectorEngineAutoDesk(ConnectorEndpointAutoDesk var1) {
      ConnectorEndpointAutoDeskAdapter var2 = var1.getConnectorEndpointAutoDeskAdapter();
      this.adapter = new ConnectorEngineAutoDeskAdapter(new ConnectorEngineAutoDeskBridge(var2.getAutoDeskConnection()));
   }

   public String getAutoDeskItemVersionId(String var1, String var2) {
      return this.adapter.getAutoDeskItemVersionId(var1, var2);
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      return this.adapter.getConnectorObjectList_workspace();
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      ConnectorInputValidatorAutoDesk var3 = new ConnectorInputValidatorAutoDesk(var2);
      var3.validatecomplete();
      return this.adapter.getConnectorXsdSchemas(var3.operation, var3.workspaceid);
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      ConnectorInputValidatorAutoDesk var4 = new ConnectorInputValidatorAutoDesk(var1);
      var4.validatecomplete();
      this.adapter.executeConnectorFileUtf8(var4.operation, var4.workspaceid, var2, var3);
   }
}
