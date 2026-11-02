package org.etlforge.integration.server.engine.connector.autodesk;

import org.etlforge.integration.server.engine.connector.autodesk.adapter.ConnectorEndpointAutoDeskAdapter;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;

public class ConnectorEndpointAutoDesk extends ConnectorEndpointAbstract {
   @Override
   public ConnectorEngine newConnectorEngine() {
      return new ConnectorEngineAutoDesk(this);
   }

   public String login() {
      ConnectorEndpointAutoDeskAdapter var1 = this.getConnectorEndpointAutoDeskAdapter();
      return var1.login();
   }

   public void logout() {
      ConnectorEndpointAutoDeskAdapter var1 = this.getConnectorEndpointAutoDeskAdapter();
      var1.logout();
   }

   @Override
   public boolean testConnection() {
      ConnectorEndpointAutoDeskAdapter var1 = this.getConnectorEndpointAutoDeskAdapter();
      return var1.testConnection();
   }

   public ConnectorEndpointAutoDeskAdapter getConnectorEndpointAutoDeskAdapter() {
      ConnectorEndpointParameters var1 = this.getEndpointParameters();
      String var2 = var1.getHost();
      String var3 = var1.getUser();
      String var4 = var1.getPassword();
      String var5 = var1.getParameters().getProperty("sessionid");
      StringBuilder var6 = new StringBuilder();
      if (this.isEmpty(var2)) {
         var6.append("Autodesk service URL,");
      }

      if (this.isEmpty(var3) && this.isEmpty(var5)) {
         var6.append("User name,");
      }

      boolean var7 = var6.length() > 0;
      if (var7) {
         String var8 = "No input for " + var6.substring(0, var6.length() - 1);
         throw new ConnectorInvalidInputRuntimeException(var8);
      } else {
         return new ConnectorEndpointAutoDeskAdapter(var2, var3, var4, var5, var1.getProxySettings());
      }
   }

   private boolean isEmpty(String var1) {
      return var1 == null || var1.isEmpty();
   }
}
