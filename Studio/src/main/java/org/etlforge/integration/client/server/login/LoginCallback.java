package org.etlforge.integration.client.server.login;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.user.User;

public interface LoginCallback extends ServerCallback {
   void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3);

   void invalidCredentials(InvalidCredentialsException var1);

   @Override
   void caught(Throwable var1);

   abstract class Adapter implements LoginCallback {
      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
      }

      @Override
      public void invalidCredentials(InvalidCredentialsException var1) {
      }

      @Override
      public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
      }
   }
}
