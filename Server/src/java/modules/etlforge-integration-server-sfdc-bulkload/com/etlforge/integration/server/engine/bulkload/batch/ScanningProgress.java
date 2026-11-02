package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Preconditions;
import org.etlforge.integration.server.progress.EnterNewSectionMessage;
import org.etlforge.integration.server.progress.QuantityBasedProgressMessage;

public final class ScanningProgress {
   private static final String SECTION = "Scanning the source file";
   private final BatchProcessMonitor monitor;
   private long startTime = -1L;
   private long fileSize = -1L;

   public ScanningProgress(BatchProcessMonitor var1) {
      this.monitor = var1;
   }

   public void scanningStarted(long var1) {
      this.fileSize = var1;
      this.startTime = System.currentTimeMillis();
      this.monitor.handleMessage(new NewProgressMessage(new EnterNewSectionMessage("Scanning the source file")));
   }

   public void scanningUpdate(long var1) {
      Preconditions.checkState(this.fileSize >= 0L, "scanningStarted has not been called");
      QuantityBasedProgressMessage var3 = new QuantityBasedProgressMessage("Scanning the source file", this.startTime, this.fileSize, var1);
      var3.setMessageFormat("Now at position %d out of %d in the source file.");
      this.monitor.handleMessage(new NewProgressMessage(var3));
   }
}
