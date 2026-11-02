package org.etlforge.integration.client.admin;

import org.etlforge.integration.client.server.ServerCallback;

public interface IsRunningCallback extends ServerCallback {
   void isRunningComplete(boolean var1);
}
