package org.etlforge.integration.client.server;

import org.etlforge.application.Application;

public final class IntegrationServer {
   private static final IntegrationServer instance = new IntegrationServer();
   private volatile ServerInfo serverInfo;

   private IntegrationServer() {
   }

   public static IntegrationServer getInstance() {
      return instance;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   void setServerInfo(ServerInfo var1) {
      this.serverInfo = var1;
   }

   public <T extends ServerCall> T getServerCall(Class<T> var1) {
      return Application.containsBean(var1.getName()) ? Application.getBean(var1.getName(), var1) : null;
   }
}
