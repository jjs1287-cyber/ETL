package org.etlforge.integration.server.engine.connector.endpoint;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;

public class ConnectorEndpointTypeConfig {
   Map<String, ConnectorEndpointType> endpointTypes = Maps.newConcurrentMap();

   Map<String, ConnectorEndpointType> getConfiggedEndpointTypes() {
      return this.endpointTypes;
   }

   ConnectorEndpointTypeConfig() {
      this.readConfiggedEndpointTypes();
   }

   boolean isDriversValid() {
      for (Entry var2 : this.endpointTypes.entrySet()) {
         ConnectorEndpointType var3 = (ConnectorEndpointType)var2.getValue();
         if (var3.newConnectorEndpoint() == null) {
            return false;
         }
      }

      return true;
   }

   synchronized void readConfiggedEndpointTypes() {
      this.endpointTypes.clear();
      ConnectorEndpointType var1 = new ConnectorEndpointType("SAP", null);
      this.endpointTypes.put(var1.endpointTypeName, var1);
      var1 = new ConnectorEndpointType("MSCRM", null);
      this.endpointTypes.put(var1.endpointTypeName, var1);
      var1 = new ConnectorEndpointType("MSGP", null);
      this.endpointTypes.put(var1.endpointTypeName, var1);
      var1 = new ConnectorEndpointType("AUTODESK", null);
      this.endpointTypes.put(var1.endpointTypeName, var1);
      var1 = new ConnectorEndpointType("MSAX", null);
      this.endpointTypes.put(var1.endpointTypeName, var1);
      var1 = new ConnectorEndpointType("EBS", null);
      this.endpointTypes.put(var1.endpointTypeName, var1);
      var1 = new ConnectorEndpointType("NETSUITE", null);
      this.endpointTypes.put(var1.endpointTypeName, var1);
   }
}
