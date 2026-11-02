package com.etlforge.integration.server.engine.bulkload.batch;

public final class ScanningDoneMessage extends BatchProcessMessage {
   private final ScanningResult result;

   public ScanningDoneMessage(ScanningResult var1) {
      this.result = var1;
   }

   public ScanningResult getResult() {
      return this.result;
   }

   @Override
   public String toString() {
      return "ScanningDoneMessage[" + this.result + "]";
   }
}
