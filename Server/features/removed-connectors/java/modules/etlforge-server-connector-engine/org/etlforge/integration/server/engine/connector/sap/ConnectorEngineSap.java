package org.etlforge.integration.server.engine.connector.sap;

import java.util.List;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.sap.adapter.ConnectorEngineSapAdapter;

public class ConnectorEngineSap implements ConnectorEngine {
   private final ConnectorEngineSapAdapter adapter;

   ConnectorEngineSap(ConnectorEndpointSap var1) {
      this.adapter = new ConnectorEngineSapAdapter(var1);
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      if (var1.size() == 0) {
         return ConnectorEngineSapAdapter.getConnectorObjectListSapSystems();
      }

      ConnectorInputValidatorSap var3 = new ConnectorInputValidatorSap(var1);
      var3.validateIncomplete();
      return var3.isIDOC
         ? this.adapter.getConnectorObjectList_IDoc(var3.messageType, var2)
         : this.adapter.getConnectorObjectList_RFC(var3.isRfc, var3.isBapi, var2);
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      ConnectorInputValidatorSap var3 = new ConnectorInputValidatorSap(var2);
      var3.validatecomplete();
      return var3.isIDOC
         ? this.adapter.getConnectorXsdSchemas_IDOC(var3.messageType, var3.idocType)
         : this.adapter.getConnectorXsdSchemas_RFC(var3.functionName);
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      ConnectorInputValidatorSap var4 = new ConnectorInputValidatorSap(var1);
      var4.validatecomplete();
      if (var4.isIDOC) {
         this.adapter.executeConnector_IDOC(var4.messageType, var4.idocType, var2, var3);
      } else {
         this.adapter.executeConnector_RFC(var4.functionName, var2, var3);
      }
   }
}
