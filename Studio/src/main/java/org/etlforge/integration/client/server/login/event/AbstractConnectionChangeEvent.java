package org.etlforge.integration.client.server.login.event;

import java.util.EventObject;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginService;

abstract class AbstractConnectionChangeEvent extends EventObject {
   private final ServerInfo currentServer;
   private final LoginCredentials currentCredentials;

   public AbstractConnectionChangeEvent(LoginService var1, ServerInfo var2, LoginCredentials var3) {
      super(var1);
      this.currentServer = var2;
      this.currentCredentials = var3;
   }

   public LoginService getSource() {
      return (LoginService)super.getSource();
   }

   public ServerInfo getCurrentServer() {
      return this.currentServer;
   }

   public LoginCredentials getCurrentCredentials() {
      return this.currentCredentials;
   }
}
