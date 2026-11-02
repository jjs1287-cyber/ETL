package org.etlforge.integration.client.admin;

import java.util.EnumSet;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetStatusCallback extends ServerCallback {
   void getStatusComplete(EnumSet<ServerStatus> var1, String var2);
}
