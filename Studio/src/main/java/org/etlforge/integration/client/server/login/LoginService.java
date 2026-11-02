package org.etlforge.integration.client.server.login;

import org.etlforge.integration.client.server.login.event.LoginListener;

public interface LoginService {
   void login(LoginCredentials var1);

   void logout();

   LoginConfiguration getConfiguration();

   void setNextServerToLoginTo(String var1);

   String getNextServerToLoginTo();

   void addLoginListener(LoginListener var1);

   void removeLoginListener(LoginListener var1);

   void addCallback(LoginCallback var1);

   void removeCallback(LoginCallback var1);

   void cancelled();
}
