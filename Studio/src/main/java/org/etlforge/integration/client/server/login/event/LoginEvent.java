package org.etlforge.integration.client.server.login.event;

import com.google.common.base.Preconditions;
import java.util.EventObject;
import org.etlforge.integration.client.server.login.LoginInfo;
import org.etlforge.integration.client.server.login.LoginService;

public class LoginEvent extends EventObject {
   private final LoginInfo info;

   public LoginEvent(LoginService var1, LoginInfo var2) {
      super(var1);
      Preconditions.checkNotNull(var2, "info");
      this.info = var2;
   }

   public LoginService getSource() {
      return (LoginService)super.getSource();
   }

   public LoginInfo getInfo() {
      return this.info;
   }
}
