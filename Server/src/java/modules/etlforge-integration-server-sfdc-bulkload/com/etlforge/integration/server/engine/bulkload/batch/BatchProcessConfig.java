package com.etlforge.integration.server.engine.bulkload.batch;

public final class BatchProcessConfig {
   public static final BatchProcessConfig DEFAULT = new BatchProcessConfig(10, 500, 5, 0);
   public static final BatchProcessConfig SINGLE_THREAD;
   public final int numberOfThreads;
   public final int recordsPerBundle;
   public final int queueSize;
   public final int recordsToValidate;

   public static BatchProcessConfig withThreadNumber(int var0) {
      int var1 = Math.max(1, 50 / var0);
      return new BatchProcessConfig(var0, DEFAULT.recordsPerBundle, var1, DEFAULT.recordsToValidate);
   }

   public BatchProcessConfig(int var1, int var2, int var3, int var4) {
      this.numberOfThreads = var1;
      this.recordsPerBundle = var2;
      this.queueSize = var3;
      this.recordsToValidate = var4;
   }

   public BatchProcessConfig noValidation() {
      return this.recordsToValidate > 0 ? new BatchProcessConfig(this.numberOfThreads, this.recordsPerBundle, this.queueSize, 0) : this;
   }

   static {
      SINGLE_THREAD = new BatchProcessConfig(
         1, DEFAULT.recordsPerBundle, Math.max(DEFAULT.recordsPerBundle / DEFAULT.numberOfThreads, 1), DEFAULT.recordsToValidate
      );
   }
}
