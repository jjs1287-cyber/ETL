package org.etlforge.integration.server.engine.connector.autodesk.adapter;

import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskConnection;
import org.etlforge.util.net.ProxySettings;

public class ConnectorEndpointAutoDeskAdapter {
   final AutoDeskConnection autodeskConnection;

   public ConnectorEndpointAutoDeskAdapter(String var1, String var2, String var3, String var4, ProxySettings var5) {
      this.autodeskConnection = new AutoDeskConnection(var1, var2, var3, var4, var5);
   }

   public AutoDeskConnection getAutoDeskConnection() {
      return this.autodeskConnection;
   }

   public String login() {
      this.autodeskConnection.open();
      return this.autodeskConnection.getSessionId();
   }

   public void logout() {
      this.autodeskConnection.dispose();
   }

   public boolean testConnection() {
      return this.autodeskConnection.testConnection();
   }
}
