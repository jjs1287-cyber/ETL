package com.etlforge.integration.server.engine.bulkload.batch;

import org.etlforge.integration.server.progress.OperationProgressMessage;

public final class NewProgressMessage extends BatchProcessMessage {
   private final OperationProgressMessage progress;

   public NewProgressMessage(OperationProgressMessage var1) {
      this.progress = var1;
   }

   public OperationProgressMessage getProgressMessage() {
      return this.progress;
   }

   @Override
   public String toString() {
      return "NewProgressMessage[" + this.progress.getMessage() + " / Estimated end time: " + this.progress.getEstimatedEndTime() + "]";
   }
}
