package org.etlforge.integration.client.server.ui.login;

import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.user.User;

final class SafeCallback implements LoginCallback {
   private final LoginCallback wrappee;

   public SafeCallback(LoginCallback var1) {
      this.wrappee = var1;
   }

   @Override
   public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
      if (this.wrappee != null) {
         this.wrappee.loginSucceeded(var1, var2, var3);
      }
   }

   @Override
   public void invalidCredentials(InvalidCredentialsException var1) {
      if (this.wrappee != null) {
         this.wrappee.invalidCredentials(var1);
      }
   }

   @Override
   public void cancelled() {
      if (this.wrappee != null) {
         this.wrappee.cancelled();
      }
   }

   @Override
   public void caught(Throwable var1) {
      if (this.wrappee != null) {
         this.wrappee.caught(var1);
      }
   }
}
