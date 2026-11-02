package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Preconditions;
import java.util.logging.Logger;

public final class DefaultCancelRequestHandler implements Runnable {
   private final BatchProcess process;
   private volatile Logger logger = Logger.getAnonymousLogger();

   public DefaultCancelRequestHandler(BatchProcess var1) {
      Preconditions.checkNotNull(var1, "process");
      this.process = var1;
   }

   @Override
   public void run() {
      this.process.cancel();
      this.logRequest();
   }

   private void logRequest() {
      this.logger.severe("A worker thread detected an error condition that requires the process to be aborted.");
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }
}
