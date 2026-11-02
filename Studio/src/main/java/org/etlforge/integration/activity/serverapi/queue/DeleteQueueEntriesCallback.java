package org.etlforge.integration.activity.serverapi.queue;

import org.etlforge.integration.client.server.ServerCallback;

public interface DeleteQueueEntriesCallback extends ServerCallback {
   void entriesWereDeleted(long[] var1);
}
