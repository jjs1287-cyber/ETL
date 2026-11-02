package org.etlforge.integration.client.server.login.event;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginService;

public class LogoutRequestEvent extends AbstractConnectionChangeEvent {
   public LogoutRequestEvent(LoginService var1, ServerInfo var2, LoginCredentials var3) {
      super(var1, var2, var3);
      Preconditions.checkNotNull(var2, "currentServer");
      Preconditions.checkNotNull(var3, "currentCredentials");
   }
}
