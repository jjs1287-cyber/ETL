package org.etlforge.integration.client.database.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObjectContainer;

public interface GetDatabaseObjectCallback extends ServerCallback {
   void objectsRetrieved(DatabaseObjectContainer[] var1, BeginEndQuote var2);
}
