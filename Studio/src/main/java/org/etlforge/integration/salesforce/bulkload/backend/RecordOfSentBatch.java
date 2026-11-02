package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Preconditions;
import java.util.Date;

public final class RecordOfSentBatch {
   private final BulkLoadInstanceId instanceId;
   private final Batch batch;
   private final long timeSent;

   public RecordOfSentBatch(BulkLoadInstanceId var1, Batch var2, Date var3) {
      this.instanceId = Preconditions.checkNotNull(var1);
      this.batch = Preconditions.checkNotNull(var2);
      this.timeSent = var3.getTime();
   }

   public BulkLoadInstanceId getInstanceId() {
      return this.instanceId;
   }

   public Batch getBatch() {
      return this.batch;
   }

   public Date getTimeSent() {
      return new Date(this.timeSent);
   }

   public BatchStatus getStatus() {
      return this.batch.getStatus();
   }

   public int getNumberOfRecordsSent() {
      return this.batch.getNumberOfRecordsSent();
   }

   public int getNumberOfProcessedRecords() {
      return this.batch.getNumberOfProcessedRecords();
   }

   public int getNumberOfFailedRecords() {
      return this.batch.getNumberOfFailedRecords();
   }

   public long getBytesSent() {
      return this.batch.getBytesSent();
   }
}
