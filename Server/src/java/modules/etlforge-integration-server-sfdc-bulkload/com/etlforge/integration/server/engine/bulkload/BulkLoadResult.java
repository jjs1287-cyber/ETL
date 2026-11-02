package com.etlforge.integration.server.engine.bulkload;

public final class BulkLoadResult {
   public final boolean allBatchesSent;
   public final BulkLoadException fatalError;
   public final String logFilePath;
   public final long numberOfRecordsSent;

   public static BulkLoadResult fatalError(BulkLoadException var0) {
      return new BulkLoadResult(false, var0, null, 0L);
   }

   public static BulkLoadResult success(boolean var0, BulkLoadException var1, long var2, String var4) {
      return new BulkLoadResult(var0, var1, var4, var2);
   }

   private BulkLoadResult(boolean var1, BulkLoadException var2, String var3, long var4) {
      this.allBatchesSent = var1;
      this.fatalError = var2;
      this.logFilePath = var3;
      this.numberOfRecordsSent = var4;
   }

   @Override
   public String toString() {
      return this.fatalError != null
         ? String.format("ERROR [%d] %s", this.fatalError.getErrorCode(), this.fatalError.getMessage())
         : String.format(
            "No of records sent: %d (%s)%s",
            this.numberOfRecordsSent,
            this.allBatchesSent ? "ALL" : "INCOMPLETE",
            this.logFilePath == null ? "" : " - there were errors reported"
         );
   }
}
