package org.etlforge.integration.client.server.login.event;

import java.util.EventListener;
import org.etlforge.integration.client.server.login.ConnectionChangeVetoException;

public interface LoginListener extends EventListener {
   void loginRequested(LoginRequestEvent var1) throws ConnectionChangeVetoException;

   void postLogin(LoginEvent var1);

   void logoutRequested(LogoutRequestEvent var1) throws ConnectionChangeVetoException;

   abstract class Adapter implements LoginListener {
      @Override
      public void loginRequested(LoginRequestEvent var1) throws ConnectionChangeVetoException {
      }

      @Override
      public void logoutRequested(LogoutRequestEvent var1) throws ConnectionChangeVetoException {
      }

      @Override
      public void postLogin(LoginEvent var1) {
      }
   }
}
