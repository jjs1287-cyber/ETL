package org.etlforge.integration.loadsource.client;

import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.loadsource.client.resources.LoadSourceResources;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataCallback;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataProvider;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataResult;
import org.etlforge.system.callback.CallbackResult;

public final class LoadSourceDataJob extends UiJob implements LoadSourceDataCallback {
   private final SourceDataService service;
   private final LoadSourceDataInput input;
   private final TransformationId transformationId;
   private final CallbackResult externalCallback;
   private boolean cancelHasBeenHandled;

   public LoadSourceDataJob(SourceDataService var1, LoadSourceDataInput var2, TransformationId var3, String var4, CallbackResult var5) {
      super(getName(var4));
      this.service = var1;
      this.input = var2;
      this.transformationId = var3;
      this.externalCallback = var5;
      this.setUncaughtExceptionHandler(this);
      this.setBlockUntilDone(true);
   }

   private static String getName(String var0) {
      return LoadSourceResources.format("LoadSource.Job.Name", var0);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(LoadSourceResources.getString("LoadSource.Job.SendToServer"));
      IntegrationServer var1 = IntegrationServer.getInstance();
      LoadSourceDataProvider var2 = var1.getServerCall(LoadSourceDataProvider.class);
      var2.loadSourceDataTree(this.input, this.transformationId, this);
   }

   @Override
   protected void onJobCancelled() {
      this.cancelled();
   }

   @Override
   public void sourceDataTreeGenerated(LoadSourceDataResult var1) {
      if (!this.isCancelled()) {
         this.setMessage(LoadSourceResources.getString("LoadSource.Job.UpdateUi"));
         this.service.storeSourceData(var1);
         this.externalCallback.succeeded(this.transformationId);
      }
   }

   @Override
   public void cancelled() {
      if (!this.cancelHasBeenHandled) {
         this.cancelHasBeenHandled = true;
         this.externalCallback.cancelled(null);
      }
   }

   @Override
   public void caught(Throwable var1) {
      if (!this.isCancelled()) {
         this.setStatus(JobStatus.ERROR);
         this.setMessage(UiJobMessages.ERROR);
         this.service.storeSourceData(null);
         this.externalCallback.failed(var1);
      }
   }
}
