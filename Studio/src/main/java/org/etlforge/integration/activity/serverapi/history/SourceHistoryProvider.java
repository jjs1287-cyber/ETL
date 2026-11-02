package org.etlforge.integration.activity.serverapi.history;

import org.etlforge.integration.client.server.ServerCall;

public interface SourceHistoryProvider extends ServerCall {
   void getSourceHistory(HistoryRequest var1, GetSourceHistoryCallback var2);

   void deleteSourceHistoryEntries(long[] var1, DeleteSourceHistoryCallback var2);
}
