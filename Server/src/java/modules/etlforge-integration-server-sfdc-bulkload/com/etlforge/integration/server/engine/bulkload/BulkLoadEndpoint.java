package com.etlforge.integration.server.engine.bulkload;

public interface BulkLoadEndpoint {
   String getSessionId();

   String getCreateJobUrl();

   String getSendBatchUrl(String var1);

   String getCloseJobUrl(String var1);
}
