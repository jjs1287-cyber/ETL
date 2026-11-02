package org.etlforge.integration.activity.serverapi.history;

import org.etlforge.integration.client.server.ServerCallback;

public interface DeleteSourceHistoryCallback extends ServerCallback {
   void entriesWereDeleted(long[] var1);
}
