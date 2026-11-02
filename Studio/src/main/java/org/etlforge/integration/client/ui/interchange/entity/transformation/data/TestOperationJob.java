package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingException;
import org.etlforge.integration.debug.client.DebuggingParametersBuilder;
import org.etlforge.integration.debug.client.DefaultDebugController;
import org.etlforge.integration.debug.client.SessionRequester;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.serverapi.AbstractServerTestCallback;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.ServerTestCallbackAdapter;
import org.etlforge.integration.debug.serverapi.TestOperationProvider;

public final class TestOperationJob extends UiJob {
   private final Transformation transformation;
   private final DebugService debugService;
   private final Operation operation;
   private volatile DebugSession session;
   private final ServerTestCallback internalCallback;
   private final ServerTestCallback externalCallback;

   public TestOperationJob(Operation var1, Transformation var2, DebugService var3, ServerTestCallback var4) {
      super(getName(var1));
      this.debugService = var3;
      this.operation = var1;
      this.transformation = var2;
      this.externalCallback = var4 != null ? var4 : new ServerTestCallbackAdapter() {};
      this.internalCallback = new TestOperationJob.InternalCallback();
      this.setUncaughtExceptionHandler(this.internalCallback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("TestOperation.Starting"));

      try {
         this.requestDebugSession();
      } catch (DebuggingException var2) {
         this.internalCallback.caught(var2);
      }
   }

   private void requestDebugSession() throws DebuggingException {
      final DefaultDebugController var1 = new DefaultDebugController(this.internalCallback, TestOperationProvider.class);
      this.debugService.startNewDebuggingSession(TestOperationProvider.ID, this.operation.getID(), var1, new SessionRequester() {
         @Override
         public void requestDenied() {
            TestOperationJob.this.internalCallback.cancelled();
         }

         @Override
         public void newSessionStarted(DebugSession var1x) {
            TestOperationJob.this.prepareNewSession(var1x, var1);
            TestOperationJob.this.startTest();
         }
      });
   }

   private void prepareNewSession(DebugSession var1, DefaultDebugController var2) {
      var2.setSession(var1);
      DebuggingParametersBuilder var3 = new DebuggingParametersBuilder();
      var3.setSessionId(var1.getId()).setCommand("Run");
      var1.setParams(var3.build());
      this.session = var1;
   }

   private void startTest() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      TestOperationProvider var2 = var1.getServerCall(TestOperationProvider.class);
      var2.testOperation(this.session, this.operation.getID(), this.transformation.getID(), this.internalCallback);
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   private static String getName(Operation var0) {
      return Strings.format("TestOperation.Name", var0.getName());
   }

   private final class InternalCallback extends AbstractServerTestCallback {
      public InternalCallback() {
         super(TestOperationJob.this.debugService);
      }

      @Override
      protected void doCompletionWork(DebugSession var1, TestResult var2) {
         if (!TestOperationJob.this.isCancelled()) {
            TestOperationJob.this.setMessage(UiJobMessages.UPDATE_UI);
            TestOperationJob.this.externalCallback.completed(var1, var2);
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!TestOperationJob.this.isCancelled()) {
            this.abortSession();
            TestOperationJob.this.setStatus(JobStatus.ERROR);
            TestOperationJob.this.setMessage(UiJobMessages.ERROR);
            TestOperationJob.this.externalCallback.caught(var1);
         }
      }

      @Override
      public void cancelled() {
         this.abortSession();
         TestOperationJob.this.externalCallback.cancelled();
      }

      private void abortSession() {
         if (TestOperationJob.this.session != null) {
            this.abort(TestOperationJob.this.session);
         }
      }
   }
}
