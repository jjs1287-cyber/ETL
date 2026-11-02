package org.etlforge.integration.server.progress;

import java.util.Date;

public interface OperationProgressMessage {
   String getSection();

   String getMessage();

   int getPercentageDone();

   long getEstimatedTimeLeftInSeconds();

   Date getEstimatedEndTime();
}
