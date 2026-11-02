package com.etlforge.integration.server.engine.bulkload.batch;

public final class ConsoleMonitor extends AbstractBatchProcessMonitor {
   @Override
   protected void doCustomProcessing(BatchProcessMessage var1) {
      System.out.println(var1);
   }
}
