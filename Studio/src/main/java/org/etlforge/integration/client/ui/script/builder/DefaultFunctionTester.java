package org.etlforge.integration.client.ui.script.builder;

import java.util.concurrent.Future;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.client.ui.script.debug.ScriptDebugModel;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.debug.client.DebugController;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingParameters;
import org.etlforge.integration.debug.client.DebuggingParametersBuilder;
import org.etlforge.integration.debug.client.DebuggingState;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.TestScriptProvider;
import org.etlforge.util.id.KongaGuid;

public final class DefaultFunctionTester implements FunctionTester {
   private final TestScriptProvider testProvider;
   private final ScriptDebugModel debugModel;
   private final ExpressionTranslator translator;

   public DefaultFunctionTester(TestScriptProvider var1, ScriptDebugModel var2, ExpressionTranslator var3) {
      this.testProvider = var1;
      this.debugModel = var2;
      this.translator = var3;
   }

   @Override
   public ScriptDataElementsModel getDataElements() {
      return this.debugModel.getDataElementsModel();
   }

   @Override
   public final Future<?> testFunction(String var1, ScriptDataElementsModel var2, ServerTestCallback var3) {
      String var4 = this.translator.fromDisplay(var1);
      DefaultFunctionTester.Worker var5 = new DefaultFunctionTester.Worker(var4, var2, var3);
      var5.execute();
      return var5;
   }

   private static class SessionImpl implements DebugSession {
      private final KongaGuid guid = KongaGuid.createGuid();
      private DebuggingParameters params;

      public SessionImpl(ScriptDataElementsModel var1) {
         DebuggingParametersBuilder var2 = new DebuggingParametersBuilder();
         var2.setSessionId(this.guid).setCommand("Run").setDataElements(var1.getDataElements());
         this.params = var2.build();
      }

      @Override
      public String getTestProviderId() {
         return TestScriptProvider.ID;
      }

      @Override
      public boolean isIsolatedTest() {
         return true;
      }

      @Override
      public IntegrationEntityId getDebuggeeId() {
         return null;
      }

      @Override
      public KongaGuid getId() {
         return this.guid;
      }

      @Override
      public DebuggingParameters getParams() {
         return this.params;
      }

      @Override
      public void setParams(DebuggingParameters var1) {
         this.params = var1;
      }

      @Override
      public void aborted() {
      }

      @Override
      public void addListener(DebugSessionListener var1) {
      }

      @Override
      public void completed(TestResult var1) {
      }

      @Override
      public DebugController getController() {
         return null;
      }

      @Override
      public TestResult getResult() {
         return null;
      }

      @Override
      public DebuggingState getState() {
         return null;
      }

      @Override
      public TargetNode getTargetNode() {
         return null;
      }

      @Override
      public void setTargetNode(TargetNode var1) {
      }

      @Override
      public void setSourceDataGuid(KongaGuid var1) {
      }

      @Override
      public KongaGuid getSourceDataGuid() {
         return null;
      }

      @Override
      public void paused(TestResult var1) {
      }

      @Override
      public void removeListener(DebugSessionListener var1) {
      }

      @Override
      public void resumed() {
      }
   }

   private class Worker extends KongaSwingWorker<Void, Void> {
      private final String expression;
      private final ScriptDataElementsModel dataElements;
      private final ServerTestCallback callback;

      public Worker(String var2, ScriptDataElementsModel var3, ServerTestCallback var4) {
         this.expression = ScriptConstants.wrapInTags(var2);
         this.dataElements = var3;
         this.callback = var4;
      }

      protected Void doInBackground() throws Exception {
         DefaultFunctionTester.this.testProvider.testExpression(new DefaultFunctionTester.SessionImpl(this.dataElements), this.expression, this.callback);
         return null;
      }

      @Override
      protected void finished() {
         if (this.isCancelled()) {
            this.callback.cancelled();
         }
      }

      @Override
      protected void onFailure(Throwable var1) {
         this.callback.caught(var1);
      }
   }
}
