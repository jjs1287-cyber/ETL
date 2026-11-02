package org.etlforge.integration.client.ui.core.window.action;

import org.etlforge.application.DebugMode;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginService;

public final class LoginToDebugServerAction extends LoginAction {
   public static final String ID = "action.core.LoginDebug";

   public LoginToDebugServerAction(ServerManager var1) {
      super(var1, "action.core.LoginDebug");
      DebugMode.configureDebugModeOnlyAction(this);
   }

   @Override
   protected void setInitialLoginState(LoginService var1) {
      var1.setNextServerToLoginTo("localhost:8888");
   }
}
