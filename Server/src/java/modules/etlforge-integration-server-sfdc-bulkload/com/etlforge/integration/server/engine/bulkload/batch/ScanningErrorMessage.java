package com.etlforge.integration.server.engine.bulkload.batch;

public final class ScanningErrorMessage extends BatchProcessMessage {
   private final Exception error;

   public ScanningErrorMessage(Exception var1) {
      this.error = var1;
   }

   public Exception getError() {
      return this.error;
   }
}
