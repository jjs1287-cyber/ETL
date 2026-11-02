package org.etlforge.integration.server.engine.connector.autodesk.rest;

import org.etlforge.util.net.ProxySettings;

public class AutoDeskConnection {
   private final AutoDeskEndpoint autoDeskEndpoint;

   public AutoDeskConnection(String var1, String var2, String var3, String var4, ProxySettings var5) {
      this.autoDeskEndpoint = new AutoDeskEndpoint(var5, var1, var2, var3, var4);
   }

   public boolean testConnection() {
      try {
         this.openImpl();
         return true;
      } finally {
         this.dispose();
      }
   }

   private void openImpl() {
      this.autoDeskEndpoint.loginImpl();
   }

   public void open() {
      this.autoDeskEndpoint.login();
   }

   public void close() {
      this.autoDeskEndpoint.logout();
   }

   public void dispose() {
      this.autoDeskEndpoint.logoutImpl();
   }

   public AutoDeskEndpoint getAutoDeskEndpoint() {
      return this.autoDeskEndpoint;
   }

   public String getSessionId() {
      return this.autoDeskEndpoint.autodeskSession == null ? null : this.autoDeskEndpoint.autodeskSession.getCookie();
   }
}
