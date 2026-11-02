package org.etlforge.integration.debug.client;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.debug.serverapi.ServerTestCall;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;

public final class DefaultDebugController implements DebugController {
   private final Class<? extends ServerTestCall> serverCallClass;
   private final ServerTestCallback callback;
   private DebugSession session;

   public DefaultDebugController(ServerTestCallback var1, Class<? extends ServerTestCall> var2) {
      Preconditions.checkNotNull(var1, "callback");
      Preconditions.checkNotNull(var2, "serverCallClass");
      this.callback = var1;
      this.serverCallClass = var2;
   }

   public void setSession(DebugSession var1) {
      Preconditions.checkNotNull(var1, "session");
      this.session = var1;
   }

   @Override
   public void pause() {
      Alert.error("Sorry, not implemented yet.", "Not implemented yet.");
   }

   @Override
   public void resume(DebuggingParameters var1) {
      DebuggingParametersBuilder var2 = new DebuggingParametersBuilder(var1);
      this.session.setParams(var2.setCommand("continue").build());
      this.submitContinueJob();
   }

   @Override
   public void step(DebuggingParameters var1) {
      DebuggingParametersBuilder var2 = new DebuggingParametersBuilder(var1);
      this.session.setParams(var2.setCommand("step").build());
      this.submitContinueJob();
   }

   @Override
   public void stop() {
      this.callback.cancelled();
      this.submitStopJob();
   }

   @Override
   public void stopAndWait() throws InterruptedException {
      this.callback.cancelled();
      this.submitStopJobAndWait();
   }

   private void submitContinueJob() {
      new DefaultDebugController.ContinueJob().submit();
      this.callback.resumed(this.session);
   }

   private void submitStopJob() {
      Runnable var1 = this.createStopJob();
      Application.getWorker().submitForParallel(var1);
   }

   private void submitStopJobAndWait() throws InterruptedException {
      Runnable var1 = this.createStopJob();
      Application.getWorker()
         .submitForParallelAndWait(var1, new LoggingExceptionHandler(DefaultDebugController.class, ErrorLevel.ATTENTION, "An unexpected error occurred"));
   }

   private Runnable createStopJob() {
      return new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            IntegrationServer var1 = IntegrationServer.getInstance();
            ServerTestCall var2 = var1.getServerCall(DefaultDebugController.this.serverCallClass);
            var2.stopTest(DefaultDebugController.this.session);
         }
      };
   }

   private final class ContinueJob extends UiJob {
      public ContinueJob() {
         super("Continuing the test");
         this.setBlockUntilDone(true);
         String var2 = "Running the test";
         if (!ClientInfo.isDataLoader()) {
            var2 = var2 + " on the server";
         }

         this.setMessage(var2);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         IntegrationServer var1 = IntegrationServer.getInstance();
         ServerTestCall var2 = var1.getServerCall(DefaultDebugController.this.serverCallClass);
         var2.resumeTest(DefaultDebugController.this.session, DefaultDebugController.this.callback);
      }

      @Override
      protected void onJobCancelled() {
         DefaultDebugController.this.stop();
      }
   }
}
