package com.etlforge.integration.client.bulkload.serverapi;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;

public interface ListBatchesCallback extends ServerCallback {
   void gotBatches(List<RecordOfSentBatch> var1);
}
