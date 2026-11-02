package org.etlforge.integration.server.engine.connector.msgp;

import java.util.List;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.msgp.adapter.MsGPConnectorEngineAdpater;

public class ConnectorEngineMsGP implements ConnectorEngine {
   private final MsGPConnectorEngineAdpater adapter;

   ConnectorEngineMsGP(ConnectorEndpointMsGP var1) {
      this.adapter = new MsGPConnectorEngineAdpater(var1);
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      ConnectorInputValidatorMsGP var3 = new ConnectorInputValidatorMsGP(var1);
      return this.adapter.getConnectorObjectList(var3.gpOperation, var2);
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      ConnectorInputValidatorMsGP var3 = new ConnectorInputValidatorMsGP(var2);
      var3.validatecomplete();
      return this.adapter.getConnectorSchemaFiles(var1, var3.gpModule);
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      ConnectorInputValidatorMsGP var4 = new ConnectorInputValidatorMsGP(var1);
      var4.validatecomplete();
      this.adapter.executeConnectorFileUtf8(var4.gpOperation, var2, var3);
   }
}
