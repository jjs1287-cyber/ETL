package org.etlforge.integration.server.engine.connector.msax;

import java.util.List;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.msax.adapter.ConnectorEngineMsAXAdapter;

public class ConnectorEngineMsAX implements ConnectorEngine {
   private final ConnectorEngineMsAXAdapter adapter;

   ConnectorEngineMsAX(ConnectorEndpointMsAX var1) {
      this.adapter = new ConnectorEngineMsAXAdapter(var1);
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      ConnectorInputValidatorMsAX var3 = new ConnectorInputValidatorMsAX(var1);
      var3.validateWsdlurl();
      return this.adapter.getConnectorObjectList("operations", var3.wsdlurl);
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      ConnectorInputValidatorMsAX var3 = new ConnectorInputValidatorMsAX(var2);
      var3.validateComplete();
      return this.adapter.getConnectorSchemaFiles("operationschemas", var1, var3.wsdlurl, var3.serviceName, var3.operation);
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      ConnectorInputValidatorMsAX var4 = new ConnectorInputValidatorMsAX(var1);
      var4.validateComplete();
      this.adapter.executeConnectorFileUtf8("executeoperation", var4.wsdlurl, var4.serviceName, var4.operation, var2, var3);
   }
}
