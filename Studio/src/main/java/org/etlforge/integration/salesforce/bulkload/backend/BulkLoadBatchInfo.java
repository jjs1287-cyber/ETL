package org.etlforge.integration.salesforce.bulkload.backend;

public class BulkLoadBatchInfo {
   private static final int statusMessageColumnLength = 4000;
   private BatchStatus statusEnum;
   private String JobId;
   private String BatchId;
   private String statusMessage;
   private long numberOfRecordsSent;
   private long numberRecordsProcessed;
   private long failureRecords;
   private long successRecords;
   private long start_pos;
   private long end_pos;

   private void init(String var1, String var2, long var3, long var5, long var7) {
      this.JobId = var1;
      this.BatchId = var2;
      this.numberRecordsProcessed = var3;
      this.statusMessage = "";
      this.start_pos = var5;
      this.end_pos = var7;
   }

   public BulkLoadBatchInfo(String var1, String var2, int var3, long var4, long var6, long var8) {
      this.init(var1, var2, var4, var6, var8);
      this.statusEnum = BatchStatus.ConvertToEnum(var3);
   }

   public BulkLoadBatchInfo(String var1, String var2, String var3, long var4, long var6, long var8) {
      this.init(var1, var2, var4, var6, var8);
      this.statusEnum = BatchStatus.ConvertToEnum(var3);
   }

   public String getJobId() {
      return this.JobId;
   }

   public String getBatchId() {
      return this.BatchId;
   }

   public String getstatusMessage() {
      return this.statusMessage;
   }

   public void setStatusEnum(BatchStatus var1) {
      this.statusEnum = var1;
   }

   public void setStatusMessage(String var1) {
      if (var1.length() > 4000) {
         this.statusMessage = var1.substring(0, 4000) + "...";
      } else {
         this.statusMessage = var1;
      }
   }

   public void setNumberRecordsProcessed(long var1) {
      this.numberRecordsProcessed = var1;
   }

   public long getNumberRecordsProcessed() {
      return this.numberRecordsProcessed;
   }

   public BatchStatus getStatusEnum() {
      return this.statusEnum;
   }

   public void setStartPos(long var1) {
      this.start_pos = var1;
   }

   public void setEndPos(long var1) {
      this.end_pos = var1;
   }

   public long getStartPos() {
      return this.start_pos;
   }

   public long getEndPos() {
      return this.end_pos;
   }

   public boolean isDone() {
      return this.statusEnum != BatchStatus.QUEUED && this.statusEnum != BatchStatus.INPROGRESS;
   }

   public void setSuccessRecords(long var1) {
      this.successRecords = var1;
   }

   public long getSuccessRecords() {
      return this.successRecords;
   }

   public void setFailureRecords(long var1) {
      this.failureRecords = var1;
   }

   public long getFailureRecords() {
      return this.failureRecords;
   }

   public long getNumberOfRecordsSent() {
      return this.numberOfRecordsSent;
   }

   public void setNumberOfRecordsSent(long var1) {
      this.numberOfRecordsSent = var1;
   }

   public RecordCounts getRecordCounts() {
      int var1 = this.statusEnum != BatchStatus.FAILED && this.statusEnum != BatchStatus.NOTPROCESSED
         ? (int)this.numberRecordsProcessed
         : (int)this.numberOfRecordsSent;
      return new RecordCounts((int)this.numberOfRecordsSent, var1, (int)this.successRecords, (int)this.failureRecords);
   }
}
