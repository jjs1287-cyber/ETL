package org.etlforge.integration.activity.client.log;

public final class OperationStats {
   public static final OperationStats NONE = new OperationStats(0, 0, 0L, 0L, 0L, 0L);
   public final int numberOfSourceFiles;
   public final int numberOfTargetFiles;
   public final long sourceTransferBytes;
   public final long targetTransferBytes;
   public final long numberOfSourceRecords;
   public final long numberOfTargetRecords;

   public OperationStats(int var1, int var2, long var3, long var5, long var7, long var9) {
      this.numberOfSourceFiles = var1;
      this.numberOfTargetFiles = var2;
      this.sourceTransferBytes = var3;
      this.targetTransferBytes = var5;
      this.numberOfSourceRecords = var7;
      this.numberOfTargetRecords = var9;
   }

   public static OperationStats.Builder build() {
      return new OperationStats.Builder();
   }

   public static class Builder {
      private int numberOfSourceFiles;
      private int numberOfTargetFiles;
      private long sourceTransferBytes;
      private long targetTransferBytes;
      private long numberOfSourceRecords;
      private long numberOfTargetRecords;

      public OperationStats.Builder sourceFiles(int var1) {
         this.numberOfSourceFiles = var1;
         return this;
      }

      public OperationStats.Builder targetFiles(int var1) {
         this.numberOfTargetFiles = var1;
         return this;
      }

      public OperationStats.Builder sourceTransferBytes(long var1) {
         this.sourceTransferBytes = var1;
         return this;
      }

      public OperationStats.Builder targetTransferBytes(long var1) {
         this.targetTransferBytes = var1;
         return this;
      }

      public OperationStats.Builder sourceRecords(long var1) {
         this.numberOfSourceRecords = var1;
         return this;
      }

      public OperationStats.Builder targetRecords(long var1) {
         this.numberOfTargetRecords = var1;
         return this;
      }

      public OperationStats done() {
         return new OperationStats(
            this.numberOfSourceFiles,
            this.numberOfTargetFiles,
            this.sourceTransferBytes,
            this.targetTransferBytes,
            this.numberOfSourceRecords,
            this.numberOfTargetRecords
         );
      }
   }
}
