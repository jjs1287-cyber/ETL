package com.etlforge.integration.server.engine.bulkload.batch;

import org.etlforge.integration.salesforce.bulkload.backend.Batch;

public final class BatchSentMessage extends BatchProcessMessage {
   private final Batch batch;

   public BatchSentMessage(Batch var1) {
      this.batch = var1;
   }

   public Batch getBatch() {
      return this.batch;
   }

   public int getNumberOfRecords() {
      return this.batch.getNumberOfRecordsSent();
   }

   @Override
   public String toString() {
      return "BatchSentMessage[" + this.batch + "]";
   }
}
