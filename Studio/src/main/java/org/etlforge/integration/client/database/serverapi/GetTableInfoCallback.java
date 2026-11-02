package org.etlforge.integration.client.database.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.database.info.DatabaseObjectContainer;

public interface GetTableInfoCallback extends ServerCallback {
   void tableInfoRetrieved(DatabaseObjectContainer[] var1);
}
