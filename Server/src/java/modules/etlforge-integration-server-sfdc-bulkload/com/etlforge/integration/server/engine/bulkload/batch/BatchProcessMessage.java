package com.etlforge.integration.server.engine.bulkload.batch;

public abstract class BatchProcessMessage {
   public static final BatchProcessMessage LAST_BATCH_SENT = new BatchProcessMessage.Constant("LAST_BATCH_SENT");
   public static final BatchProcessMessage REQUEST_CANCEL = new BatchProcessMessage.Constant("CANCEL_REQUESTED");
   public static final BatchProcessMessage FAILED = new BatchProcessMessage.Constant("FAILED");

   private static class Constant extends BatchProcessMessage {
      private final String name;

      public Constant(String var1) {
         this.name = var1;
      }

      @Override
      public String toString() {
         return this.name;
      }
   }
}
