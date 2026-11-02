package org.etlforge.application.ui;

import java.awt.EventQueue;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public class BusyWorker {
   private final WaitService waitService;
   private final Runnable job;
   private volatile WaitLock waitLock;
   private int delay;

   public BusyWorker(WaitService var1) {
      this(var1, null);
   }

   public BusyWorker(WaitService var1, Runnable var2) {
      this.waitService = var1 != null ? var1 : WaitService.NONE;
      this.job = var2;
   }

   public final void setDelay(int var1) {
      this.delay = var1;
   }

   public final void run() {
      this.startWait();
      EventQueue.invokeLater(new BusyWorker.Wrapper());
   }

   public final void runInCallingThread() {
      this.startWait();
      new BusyWorker.Wrapper().run();
   }

   protected void doWork() {
      if (this.job != null) {
         this.job.run();
      }
   }

   private void startWait() {
      this.waitLock = this.waitService.startWait(this.delay);
   }

   private class Wrapper implements Runnable {
      private Wrapper() {
      }

      @Override
      public void run() {
         try {
            BusyWorker.this.doWork();
         } finally {
            BusyWorker.this.waitLock.release();
         }
      }
   }
}
