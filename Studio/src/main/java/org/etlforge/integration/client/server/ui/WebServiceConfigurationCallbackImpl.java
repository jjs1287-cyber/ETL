package org.etlforge.integration.client.server.ui;

import org.apache.commons.lang.StringUtils;
import org.etlforge.application.NonInteractiveMode;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginConfiguration;
import org.etlforge.integration.client.server.login.LoginRequest;
import org.etlforge.integration.client.server.login.LoginRequestHandler;
import org.etlforge.integration.client.server.ui.login.PoolingLoginRequestHandler;
import org.etlforge.integration.client.server.webservice.WebServiceConfigurationCallback;
import org.etlforge.system.callback.CallbackResult;

public class WebServiceConfigurationCallbackImpl implements WebServiceConfigurationCallback {
   private final LoginRequestHandler loginHandler;
   private final LoginConfiguration loginConfig;

   public WebServiceConfigurationCallbackImpl(ServerManager var1) {
      this.loginHandler = new PoolingLoginRequestHandler(var1);
      this.loginConfig = var1.getLoginService().getConfiguration();
   }

   @Override
   public boolean assureThatWeHaveLoggedIn(CallbackResult var1) {
      String var2 = this.loginConfig.getUserName();
      String var3 = this.loginConfig.getServerName();
      boolean var4 = true;
      if (StringUtils.isEmpty(var2) || StringUtils.isEmpty(var3)) {
         var4 = this.login(var1);
      }

      return var4;
   }

   private boolean login(CallbackResult var1) {
      if (NonInteractiveMode.on()) {
         var1.failed(new Exception("Cannot launch the login dialog in non-interactive mode."));
         return false;
      } else {
         LoginRequest var2 = this.loginHandler.loginRequested(var1);
         return var2.isGranted();
      }
   }
}
