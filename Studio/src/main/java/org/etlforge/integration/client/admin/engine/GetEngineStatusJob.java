package org.etlforge.integration.client.admin.engine;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.GetStatusCallback;
import org.etlforge.integration.client.admin.ServerStatus;

public final class GetEngineStatusJob extends AbstractEngineJob {
   private final Admin.StatusDetail detailLevel;

   public GetEngineStatusJob(ServerEngine var1, Admin var2) {
      this(var1, var2, Admin.StatusDetail.NORMAL);
   }

   public GetEngineStatusJob(ServerEngine var1, Admin var2, Admin.StatusDetail var3) {
      super(var1, var2, "Getting status of " + var1.getName());
      Preconditions.checkNotNull(var3, "detailLevel");
      this.detailLevel = var3;
      var1.setGettingStatus(true);
   }

   @Override
   protected AbstractEngineJob.CallbackBase createCallback() {
      return new GetEngineStatusJob.Callback();
   }

   @Override
   protected EnumSet<ServerStatus> getErrorStatus() {
      return EnumSet.of(ServerStatus.ERROR, ServerStatus.UNDEFINED);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.updateEngine(EnumSet.of(ServerStatus.UNDEFINED), this.getName() + "...");
      this.setMessage("Retrieving the status from the server");
      Admin var1 = this.getServerApi();
      this.getEngine().requestStatus(var1, this.detailLevel, (GetStatusCallback)this.getCallback());
   }

   private class Callback extends AbstractEngineJob.CallbackBase implements GetStatusCallback {
      private Callback() {
      }

      @Override
      public void getStatusComplete(EnumSet<ServerStatus> var1, String var2) {
         GetEngineStatusJob.this.setCanBeCancelled(false);
         GetEngineStatusJob.this.setMayBeInterrupted(false);
         GetEngineStatusJob.this.setMessage(UiJobMessages.UPDATE_UI);
         GetEngineStatusJob.this.updateEngine(var1, var2);
      }
   }
}
