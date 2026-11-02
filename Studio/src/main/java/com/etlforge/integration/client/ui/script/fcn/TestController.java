package com.etlforge.integration.client.ui.script.fcn;

import java.awt.EventQueue;
import java.util.concurrent.Future;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.ui.Size;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class TestController {
   private final FunctionTester tester;
   private final ScriptDataElementsModel deModel;
   private WaitService waitService = WaitService.NONE;
   private volatile WaitLock waitLock;
   private TestWaitIndicator testWaitIndicator;

   public TestController(FunctionTester var1, ScriptDataElementsModel var2) {
      this.tester = var1;
      this.deModel = var2;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void setTestWaitIndicator(TestWaitIndicator var1) {
      this.testWaitIndicator = var1;
   }

   public void startTest(Expression var1) {
      this.waitLock = this.waitService.startWait(250);
      Future var2 = var1.test(this.tester, this.deModel, new TestController.TestCallback(this.waitLock));
      this.testWaitIndicator.setFuture(var2);
   }

   private static final class TestCallback implements ServerTestCallback {
      private final WaitLock waitLock;

      public TestCallback(WaitLock var1) {
         this.waitLock = var1;
      }

      @Override
      public void caught(Throwable var1) {
         this.waitLock.release();
         ErrorLog.attention(TestController.class, null, var1);
      }

      @Override
      public void cancelled() {
         this.waitLock.release();
      }

      @Override
      public void resumed(DebugSession var1) {
      }

      @Override
      public void paused(DebugSession var1, TestResult var2) {
      }

      @Override
      public void completed(DebugSession var1, final TestResult var2) {
         this.waitLock.release();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               TestCallback.this.displayResult(var2);
            }
         });
      }

      private void displayResult(TestResult var1) {
         KongaTextArea var2 = KongaTextArea.viewer(Size.rows(4), Size.columns(40));
         String var3 = var1.getErrors();
         if (StringUtils.isEmpty(var3)) {
            var2.setText(var1.getResult());
            var2.setCaretPosition(0);
            Alert.info(var2.asUiProvider(), "Test Result");
         } else {
            var2.setText(var1.getErrors());
            var2.setCaretPosition(0);
            Alert.error(var2.asUiProvider(), "Error");
         }
      }

      @Override
      public void aborted(DebugSession var1) {
         this.waitLock.release();
      }
   }
}
