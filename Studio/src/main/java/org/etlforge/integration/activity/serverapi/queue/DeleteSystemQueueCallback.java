package org.etlforge.integration.activity.serverapi.queue;

import org.etlforge.integration.client.server.ServerCallback;

public interface DeleteSystemQueueCallback extends ServerCallback {
   void systemQueueWasDeleted();
}
