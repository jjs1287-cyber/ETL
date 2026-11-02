package org.etlforge.integration.client.server;

import java.util.EventObject;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.user.User;

public final class ServerConnectionEvent extends EventObject {
   private final ConnectionStatus status;
   private final ServerInfo serverInfo;
   private final LoginCredentials credentials;
   private final User user;

   public ServerConnectionEvent(ServerConnectionService var1, ConnectionStatus var2, ServerInfo var3, LoginCredentials var4, User var5) {
      super(var1);
      this.status = var2;
      this.serverInfo = var3;
      this.credentials = var4;
      this.user = var5;
   }

   public final ConnectionStatus getStatus() {
      return this.status;
   }

   public final boolean isConnected() {
      return this.status == ConnectionStatus.CONNECTED;
   }

   public final ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   public final String getServerName() {
      return this.serverInfo != null ? this.serverInfo.getName() : null;
   }

   public final LoginCredentials getCredentials() {
      return this.credentials;
   }

   public final User getUser() {
      return this.user;
   }
}
