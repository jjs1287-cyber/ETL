package org.etlforge.integration.client.datalocation.connection.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.database.BeginEndQuote;

public interface DatabaseConnectionCallback extends ServerCallback {
   void connected(BeginEndQuote var1);
}
