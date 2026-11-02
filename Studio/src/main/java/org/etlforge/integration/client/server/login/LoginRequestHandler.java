package org.etlforge.integration.client.server.login;

import org.etlforge.system.callback.CallbackResult;

public interface LoginRequestHandler {
   LoginRequest loginRequested(CallbackResult var1);
}
