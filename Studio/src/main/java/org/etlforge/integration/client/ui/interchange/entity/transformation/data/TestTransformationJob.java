package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingException;
import org.etlforge.integration.debug.client.DebuggingParametersBuilder;
import org.etlforge.integration.debug.client.DefaultDebugController;
import org.etlforge.integration.debug.client.SessionRequester;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.debug.serverapi.AbstractServerTestCallback;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.ServerTestCallbackAdapter;
import org.etlforge.integration.debug.serverapi.TestTransformationProvider;

public final class TestTransformationJob extends UiJob {
   private final SourceDataIdentifier sourceDataToken;
   private final DebugService debugService;
   private final Transformation transformation;
   private final TestTransformationJob.InternalCallback internalCallback;
   private final ServerTestCallback externalCallback;
   private volatile DebugSession session;

   public TestTransformationJob(Transformation var1, SourceDataIdentifier var2, DebugService var3, ServerTestCallback var4) {
      super(getName(var1));
      this.transformation = var1;
      this.sourceDataToken = var2;
      this.debugService = var3;
      this.externalCallback = var4 != null ? var4 : new ServerTestCallbackAdapter() {};
      this.internalCallback = new TestTransformationJob.InternalCallback();
      this.setUncaughtExceptionHandler(this.internalCallback);
      this.setBlockUntilDone(true);
   }

   private static String getName(Transformation var0) {
      return Strings.format("TestTransformation.Name", var0.getName());
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("TestTransformation.Starting"));

      try {
         this.requestDebugSession();
      } catch (DebuggingException var2) {
         this.internalCallback.caught(var2);
      }
   }

   private void requestDebugSession() throws DebuggingException {
      final DefaultDebugController var1 = new DefaultDebugController(this.internalCallback, TestTransformationProvider.class);
      this.debugService.startNewDebuggingSession(TestTransformationProvider.ID, this.transformation.getID(), var1, new SessionRequester() {
         @Override
         public void requestDenied() {
            TestTransformationJob.this.internalCallback.cancelled();
         }

         @Override
         public void newSessionStarted(DebugSession var1x) {
            TestTransformationJob.this.prepareNewSession(var1x, var1);
            TestTransformationJob.this.startTest();
         }
      });
   }

   private void prepareNewSession(DebugSession var1, DefaultDebugController var2) {
      if (this.sourceDataToken != null) {
         var1.setSourceDataGuid(this.sourceDataToken.getGuid());
      }

      var2.setSession(var1);
      DebuggingParametersBuilder var3 = new DebuggingParametersBuilder();
      var3.setSessionId(var1.getId()).setCommand("Run");
      var1.setParams(var3.build());
      this.session = var1;
   }

   private void startTest() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      TestTransformationProvider var2 = var1.getServerCall(TestTransformationProvider.class);
      var2.testTransformation(this.session, this.sourceDataToken, null, this.internalCallback);
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   private final class InternalCallback extends AbstractServerTestCallback {
      public InternalCallback() {
         super(TestTransformationJob.this.debugService);
      }

      @Override
      public void doCompletionWork(DebugSession var1, TestResult var2) {
         if (!TestTransformationJob.this.isCancelled()) {
            TestTransformationJob.this.setMessage(UiJobMessages.UPDATE_UI);
            TestTransformationJob.this.externalCallback.completed(var1, var2);
         }
      }

      @Override
      public void cancelled() {
         this.abortSession();
         TestTransformationJob.this.externalCallback.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         if (!TestTransformationJob.this.isCancelled()) {
            this.abortSession();
            TestTransformationJob.this.setStatus(JobStatus.ERROR);
            TestTransformationJob.this.setMessage(UiJobMessages.ERROR);
            TestTransformationJob.this.externalCallback.caught(var1);
         }
      }

      private void abortSession() {
         if (TestTransformationJob.this.session != null) {
            this.abort(TestTransformationJob.this.session);
         }
      }
   }
}
