package org.etlforge.integration.client.server.login.event;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginService;

public class LoginRequestEvent extends AbstractConnectionChangeEvent {
   private final LoginCredentials newCredentials;

   public LoginRequestEvent(LoginService var1, ServerInfo var2, LoginCredentials var3, LoginCredentials var4) {
      super(var1, var2, var3);
      Preconditions.checkNotNull(var4, "newCredentials");
      this.newCredentials = var4;
   }

   public LoginCredentials getNewCredentials() {
      return this.newCredentials;
   }
}
