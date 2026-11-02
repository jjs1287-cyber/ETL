package com.etlforge.integration.server.engine.bulkload;

import java.io.IOException;
import java.sql.SQLException;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;
import org.etlforge.system.Disposable;

public interface BulkLoadResultProcess extends Disposable {
   boolean isDone();

   RecordCounts ResultProcess() throws SQLException, IOException, Exception;

   void waitForFuture() throws Exception;
}
