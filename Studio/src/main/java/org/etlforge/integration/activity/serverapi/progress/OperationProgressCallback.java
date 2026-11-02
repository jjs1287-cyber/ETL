package org.etlforge.integration.activity.serverapi.progress;

import java.util.List;
import org.etlforge.integration.activity.client.progress.OperationProgressMessage;
import org.etlforge.integration.client.server.ServerCallback;

public interface OperationProgressCallback extends ServerCallback {
   void handleProgressMessages(List<OperationProgressMessage> var1);
}
