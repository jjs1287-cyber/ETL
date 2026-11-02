package org.etlforge.integration.client.ui.script.debug;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.script.ScriptStatus;
import org.etlforge.integration.data.script.ScriptValidator;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingException;
import org.etlforge.integration.debug.client.DefaultDebugController;
import org.etlforge.integration.debug.client.SessionRequester;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.serverapi.AbstractServerTestCallback;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.TestScriptProvider;

final class Debugger {
   private final ScriptDebugModel model;

   public Debugger(ScriptDebugModel var1) {
      this.model = var1;
   }

   public void start() {
      if (this.validate()) {
         this.model.setStartTestEnabled(false);
         this.model.setDebugMessage("The expression is valid. Testing it...");
         this.doTestOnServer();
      }
   }

   private boolean validate() {
      this.model.setDebugMessage("");
      ScriptValidator var1 = new ScriptValidator();
      var1.setSourceDataElementContainer(this.model.getSourceDataElementContainer());
      String var2 = this.model.getExpressionToTest();
      ScriptStatus var3 = var1.validate(var2);
      if (var3.isInvalid()) {
         this.model.setDebugMessage(var3.getMessage());
         return false;
      } else {
         return true;
      }
   }

   private void doTestOnServer() {
      try {
         final Debugger.CallbackImpl var1 = new Debugger.CallbackImpl();
         final DefaultDebugController var2 = new DefaultDebugController(var1, TestScriptProvider.class);
         this.model.startNewSession(TestScriptProvider.ID, var2, "Run", new SessionRequester() {
            @Override
            public void newSessionStarted(DebugSession var1x) {
               var2.setSession(var1x);
               var1.setSession(var1x);
               Debugger.StartTestJob var2x = Debugger.this.new StartTestJob(var1x, var1);
               var2x.submit();
            }

            @Override
            public void requestDenied() {
            }
         });
      } catch (DebuggingException var3) {
         ErrorLog.attention(this, "Could not start the test", var3);
      }
   }

   private class CallbackImpl extends AbstractServerTestCallback {
      private DebugSession session;

      public CallbackImpl() {
         super(Debugger.this.model.getDebugService());
      }

      public void setSession(DebugSession var1) {
         this.session = var1;
      }

      @Override
      protected void doCompletionWork(DebugSession var1, TestResult var2) {
         Debugger.this.model.getCallback().completed(var1, var2);
      }

      @Override
      public void cancelled() {
         this.abort(this.session);
         Debugger.this.model.getCallback().cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         this.abort(this.session);
         Debugger.this.model.getCallback().caught(var1);
      }
   }

   private class StartTestJob extends UiJob {
      private final DebugSession session;
      private final ServerTestCallback callback;

      public StartTestJob(DebugSession var2, ServerTestCallback var3) {
         super("Starting the test");
         this.session = var2;
         this.callback = var3;
         this.setBlockUntilDone(true);
         String var4 = "Running the test";
         if (!ClientInfo.isDataLoader()) {
            var4 = var4 + " on the server";
         }

         this.setMessage(var4);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         IntegrationServer var1 = IntegrationServer.getInstance();
         TestScriptProvider var2 = var1.getServerCall(TestScriptProvider.class);
         var2.testExpression(this.session, Debugger.this.model.getExpressionToTest(), this.callback);
      }

      @Override
      protected void onJobCancelled() {
         this.session.getController().stop();
      }
   }
}
