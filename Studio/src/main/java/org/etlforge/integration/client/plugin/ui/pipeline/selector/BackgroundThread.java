package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class BackgroundThread {
   private final ExecutorService thread = Executors.newFixedThreadPool(1);

   public void shutdown() {
      this.thread.shutdown();
   }

   public void execute(Runnable var1) {
      this.thread.submit(var1);
   }
}
