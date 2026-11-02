package com.etlforge.integration.server.engine.bulkload.batch;

import com.etlforge.integration.server.engine.bulkload.ErrorLog;

public interface BatchResponse {
   String getBatchId(ErrorLog var1);

   boolean isClientSideError();
}
