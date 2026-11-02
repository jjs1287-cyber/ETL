package com.etlforge.integration.client.bulkload.serverapi;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;

public interface ListInstancesCallback extends ServerCallback {
   void gotInstances(List<BulkLoadInstance> var1);
}
