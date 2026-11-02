package com.etlforge.integration.client.bulkload.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceFilter;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceId;

public interface BulkLoadInfoProvider extends ServerCall {
   void listInstances(BulkLoadInstanceFilter var1, ListInstancesCallback var2);

   void listBatches(BulkLoadInstanceId var1, ListBatchesCallback var2);
}
