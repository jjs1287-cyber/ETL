package org.etlforge.integration.client.admin.engine;

import java.util.EnumSet;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.admin.StartCallback;

public final class StartEngineJob extends AbstractEngineJob {
   private final boolean wasStopped;

   public StartEngineJob(ServerEngine var1, Admin var2) {
      super(var1, var2, "Starting " + var1.getName());
      this.wasStopped = var1.getStatus().contains(ServerStatus.STOPPED);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.updateEngine(EnumSet.of(ServerStatus.STARTING), this.getName() + "...");
      this.setMessage("Sending start request to the server");
      Admin var1 = this.getServerApi();
      this.getEngine().start(var1, (StartCallback)this.getCallback());
   }

   @Override
   protected AbstractEngineJob.CallbackBase createCallback() {
      return new StartEngineJob.Callback();
   }

   @Override
   protected EnumSet<ServerStatus> getErrorStatus() {
      return EnumSet.of(ServerStatus.ERROR);
   }

   private class Callback extends AbstractEngineJob.CallbackBase implements StartCallback {
      private Callback() {
      }

      @Override
      public void startComplete() {
         StartEngineJob.this.setCanBeCancelled(false);
         StartEngineJob.this.setMayBeInterrupted(false);
         StartEngineJob.this.setMessage(UiJobMessages.UPDATE_UI);
         StartEngineJob.this.updateEngine(EnumSet.of(ServerStatus.RUNNING), StartEngineJob.this.getEngine().getName() + " is running.");
      }

      @Override
      protected void updateEngineStatusWhenCancelled() {
         ServerStatus var1 = StartEngineJob.this.wasStopped ? ServerStatus.STOPPED : ServerStatus.UNDEFINED;
         StartEngineJob.this.getEngine().setStatus(EnumSet.of(var1));
      }
   }
}
