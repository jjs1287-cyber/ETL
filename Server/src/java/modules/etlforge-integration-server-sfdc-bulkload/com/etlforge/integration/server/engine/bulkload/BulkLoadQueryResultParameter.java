package com.etlforge.integration.server.engine.bulkload;

public class BulkLoadQueryResultParameter {
   String batchid;
   String resultid;
   private volatile long receivedRecords;

   BulkLoadQueryResultParameter(String var1, String var2) {
      this.batchid = var1;
      this.resultid = var2;
      this.setReceivedRecords(0L);
   }

   public long getReceivedRecords() {
      return this.receivedRecords;
   }

   public void setReceivedRecords(long var1) {
      this.receivedRecords = var1;
   }
}
