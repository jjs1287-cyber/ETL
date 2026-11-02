package org.etlforge.integration.server.engine.connector.ebs;

import java.util.List;
import java.util.Map;
import org.etlforge.integration.server.engine.connector.ebs.adapter.ConnectorEngineEbsAdapter;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;

public class ConnectorEngineEbs implements ConnectorEngine {
   private final ConnectorEngineEbsAdapter adapter;

   ConnectorEngineEbs(ConnectorEndpointEbs var1) {
      this.adapter = new ConnectorEngineEbsAdapter(var1);
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      ConnectorInputValidatorEbs var3 = new ConnectorInputValidatorEbs(var1);
      Map var4 = this.adapter.getObjectList(var3.catalog);
      return new ConnectorObjectList(var3.isLastList, var3.objectLabel, var4);
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      ConnectorInputValidatorEbs var3 = new ConnectorInputValidatorEbs(var2);
      var3.validateComplete();
      return this.adapter.getConnectorXsdSchemas(var3.concurrProgram);
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      ConnectorInputValidatorEbs var4 = new ConnectorInputValidatorEbs(var1);
      var4.validateComplete();
      this.adapter.executeConnectorFileUtf8(var4.concurrProgram, var2, var3);
   }
}
