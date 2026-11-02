package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

public class NetSuiteCustomSearchObject {
   private final NetSuiteConnectorObject customSearchObject;

   NetSuiteCustomSearchObject(String var1) {
      this.customSearchObject = new NetSuiteConnectorObject(var1);
   }

   public NetSuiteConnectorObject getNetSuiteConnectorObject() {
      return this.customSearchObject;
   }

   public String getKey() {
      return this.customSearchObject.getKey();
   }

   @Override
   public String toString() {
      return this.customSearchObject.getName();
   }
}
