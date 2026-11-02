package org.etlforge.integration.client.ui.operation.runner;

import com.google.common.base.Charsets;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.DoNotShowAgainMessage;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.ui.operation.jobcontrol.JobControl;
import org.etlforge.integration.client.ui.operation.jobcontrol.RunOperationCallback;
import org.etlforge.integration.client.ui.operation.prefs.RunOperationSuccessMessagePreference;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.util.zip.ZipUtils;

public final class SubmitOperationJob extends UiJob {
   private final JobControl jobControl;
   private final Operation operation;
   private final int timesToRun;
   private final SubmitOperationJob.RunOpCallback internalCallback;
   private volatile Reader sourceData;

   public SubmitOperationJob(JobControl var1, Operation var2, Reader var3, Receiver<Long> var4) {
      this(var1, var2, 1, var4);
      this.sourceData = var3;
   }

   public SubmitOperationJob(JobControl var1, Operation var2, int var3, Receiver<Long> var4) {
      super(Strings.get("RunOperation.Name"));
      this.jobControl = var1;
      this.operation = var2;
      this.timesToRun = var3;
      this.internalCallback = new SubmitOperationJob.RunOpCallback(var4);
      this.setUncaughtExceptionHandler(this.internalCallback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      if (this.sourceData != null) {
         this.runWithSourceData();
      } else {
         this.runWithDeployedSource();
      }
   }

   private void runWithSourceData() {
      try {
         String var1 = this.compressSourceData();
         this.setMessage(getSendRequestMessage(1, 1));
         this.jobControl.runOperationWithSourceData(this.operation.getID(), var1, this.internalCallback);
      } catch (Exception var2) {
         this.internalCallback.caught(var2);
      }
   }

   private static String getSendRequestMessage(int var0, int var1) {
      return var1 == 1 ? Strings.get("RunOperation.StartOnce") : Strings.format("RunOperation.StartSeveral", var0, var1);
   }

   private String compressSourceData() throws IOException, UnsupportedEncodingException {
      try {
         byte[] var1 = ZipUtils.deflateReader(this.sourceData);
         byte[] var2 = Base64.encodeBase64Chunked(var1);
         return new String(var2, Charsets.UTF_8);
      } finally {
         KongaIoUtils.close(this.sourceData);
         this.sourceData = null;
      }
   }

   private void runWithDeployedSource() {
      for (int var1 = 0; var1 < this.timesToRun && !this.isCancelled(); var1++) {
         this.setMessage(getSendRequestMessage(var1 + 1, this.timesToRun));
         this.jobControl.runOperation(this.operation.getID(), this.internalCallback);
      }
   }

   @Override
   protected void onJobCancelled() {
      KongaIoUtils.close(this.sourceData);
      this.internalCallback.cancelled();
   }

   private final class RunOpCallback extends AbstractServerCallback implements RunOperationCallback {
      private int successCount;
      private final Receiver<Long> externalCallback;

      public RunOpCallback(Receiver<Long> var2) {
         this.externalCallback = var2;
         this.setErrorTitle(Strings.format("RunOperation.Error", SubmitOperationJob.this.operation.getName()));
      }

      @Override
      public void done(long var1) {
         if (!SubmitOperationJob.this.isCancelled()) {
            if (this.areAllRequestsProcessed()) {
               this.notifyExternalCallback(var1);
               this.showSuccessMessage();
            }
         }
      }

      private synchronized boolean areAllRequestsProcessed() {
         this.successCount++;
         return this.successCount == SubmitOperationJob.this.timesToRun;
      }

      private void notifyExternalCallback(long var1) {
         this.externalCallback.handle(var1);
      }

      private void showSuccessMessage() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RunOpCallback.this.postStatusMessage();
               RunOpCallback.this.showDialog();
            }
         });
      }

      private void showDialog() {
         String var1 = Strings.format("RunOperation.Success", SubmitOperationJob.this.operation.getName());
         String var2 = UiResourceBundle.getString("OK");
         BooleanPreference var3 = RunOperationSuccessMessagePreference.INSTANCE;
         if (new DoNotShowAgainMessage(var1, var2, var3).showInDialog(null)) {
            ApplicationMessageEvent.postInformationMessage(this.getClass(), "");
         }
      }

      private void postStatusMessage() {
         String var1 = new SimpleDateFormat("HH:mm:ss").format(new Date());
         String var2 = Strings.format("RunOperation.Success.AtTime", SubmitOperationJob.this.operation.getName(), var1);
         ApplicationMessageEvent.postInformationMessage(SubmitOperationJob.class, var2, new ApplicationMessageEvent.LifeTime(5000L));
      }

      @Override
      public void caught(Throwable var1) {
         if (!SubmitOperationJob.this.isCancelled()) {
            SubmitOperationJob.this.setStatus(JobStatus.ERROR);
            SubmitOperationJob.this.setMessage(UiJobMessages.ERROR);
            super.caught(var1);
         }
      }

      @Override
      public void cancelled() {
      }
   }
}
