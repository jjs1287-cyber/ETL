package org.etlforge.integration.activity.serverapi.history;

import java.util.List;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetSourceHistoryCallback extends ServerCallback {
   void historyWasRetrieved(List<SourceHistoryEntry> var1);
}
