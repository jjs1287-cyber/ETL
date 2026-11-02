package org.etlforge.integration.client.admin.engine;

import java.util.EnumSet;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.admin.StopCallback;

public final class StopEngineJob extends AbstractEngineJob {
   private final boolean wasRunning;

   public StopEngineJob(ServerEngine var1, Admin var2) {
      super(var1, var2, "Stopping " + var1.getName());
      this.wasRunning = var1.getStatus().contains(ServerStatus.RUNNING);
   }

   @Override
   protected AbstractEngineJob.CallbackBase createCallback() {
      return new StopEngineJob.Callback();
   }

   @Override
   protected EnumSet<ServerStatus> getErrorStatus() {
      return EnumSet.of(ServerStatus.ERROR);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.updateEngine(EnumSet.of(ServerStatus.STOPPING), this.getName() + "...");
      this.setMessage("Sending stop request to the server");
      Admin var1 = this.getServerApi();
      this.getEngine().stop(var1, (StopCallback)this.getCallback());
   }

   private class Callback extends AbstractEngineJob.CallbackBase implements StopCallback {
      private Callback() {
      }

      @Override
      public void stopComplete() {
         if (!StopEngineJob.this.isCancelled()) {
            StopEngineJob.this.setCanBeCancelled(false);
            StopEngineJob.this.setMayBeInterrupted(false);
            StopEngineJob.this.setMessage(UiJobMessages.UPDATE_UI);
            StopEngineJob.this.updateEngine(EnumSet.of(ServerStatus.STOPPED), StopEngineJob.this.getEngine().getName() + " is stopped.");
         }
      }

      @Override
      protected void updateEngineStatusWhenCancelled() {
         ServerStatus var1 = StopEngineJob.this.wasRunning ? ServerStatus.RUNNING : ServerStatus.UNDEFINED;
         StopEngineJob.this.getEngine().setStatus(EnumSet.of(var1));
      }
   }
}
