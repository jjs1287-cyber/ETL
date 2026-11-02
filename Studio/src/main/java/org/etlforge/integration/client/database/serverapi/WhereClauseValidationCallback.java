package org.etlforge.integration.client.database.serverapi;

import org.etlforge.integration.client.server.ServerCallback;

public interface WhereClauseValidationCallback extends ServerCallback {
   void whereClauseIsValid();
}
