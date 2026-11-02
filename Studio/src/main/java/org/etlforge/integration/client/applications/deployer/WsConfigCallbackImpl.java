package org.etlforge.integration.client.applications.deployer;

import org.etlforge.integration.client.server.webservice.WebServiceConfigurationCallback;
import org.etlforge.system.callback.CallbackResult;

public final class WsConfigCallbackImpl implements WebServiceConfigurationCallback {
   @Override
   public boolean assureThatWeHaveLoggedIn(CallbackResult var1) {
      return true;
   }
}
