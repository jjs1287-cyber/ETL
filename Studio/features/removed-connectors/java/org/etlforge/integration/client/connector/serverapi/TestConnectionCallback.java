package org.etlforge.integration.client.connector.serverapi;

import javax.annotation.Nullable;
import org.etlforge.integration.client.server.ServerCallback;

public interface TestConnectionCallback extends ServerCallback {
   void success(@Nullable String var1);
}
