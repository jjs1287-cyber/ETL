package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryCallback;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.system.thread.PendingFutures;
import org.etlforge.ui.wait.WaitService;

public final class TestQueryController {
   private final QueryBuilderModel model;
   private final TestNetSuiteQueryApi testApi;
   private final PendingFutures runningJobs = new PendingFutures();
   private final TestQueryController.TestQueryAction testAction = new TestQueryController.TestQueryAction();
   private volatile NetSuiteEndpoint endpoint;
   private volatile ConnectorObject netsuiteObject;
   private volatile WaitService waitService = WaitService.NONE;

   public TestQueryController(QueryBuilderModel var1, TestNetSuiteQueryApi var2) {
      this.model = Preconditions.checkNotNull(var1);
      this.testApi = Preconditions.checkNotNull(var2);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   public void setEndpointAndObject(NetSuiteEndpoint var1, ConnectorObject var2) {
      this.endpoint = Preconditions.checkNotNull(var1);
      this.netsuiteObject = Preconditions.checkNotNull(var2);
      this.testAction.setEnabled(true);
   }

   public TestQueryController.TestQueryAction getTestAction() {
      return this.testAction;
   }

   public void cancelRunningJobs() {
      this.runningJobs.cancelAll();
   }

   public void testQuery() {
      Preconditions.checkState(this.endpoint != null, "Must set a NetSuite endpoint first.");
      Preconditions.checkState(this.netsuiteObject != null, "Must set a NetSuite object first");
      this.model.updateResult();
      if (!this.model.isValueValid()) {
         Alert.error(this.model.getInvalidMessage(), "Error Message");
      } else {
         TestQueryController.QueryRunner var1 = new TestQueryController.QueryRunner(this.testApi, this.endpoint, this.netsuiteObject, this.model.toXml());
         TestQueryController.ResultDisplayer var2 = new TestQueryController.ResultDisplayer();
         Future var3 = Computation.create(var1, var2)
            .swingWorkerBuilder()
            .withErrorHandler(new LoggingExceptionHandler(this.getClass(), ErrorLevel.ATTENTION))
            .withWaitMessage(this.waitService, "Testing the query")
            .buildAndExecute();
         this.runningJobs.addJob(var3);
         var2.setJob(var3);
      }
   }

   private static class QueryRunner implements Callable<TestNetSuiteQueryApi.Result> {
      private final TestNetSuiteQueryApi testApi;
      private final NetSuiteEndpoint endpoint;
      private final ConnectorObject object;
      private final String xml;

      public QueryRunner(TestNetSuiteQueryApi var1, NetSuiteEndpoint var2, ConnectorObject var3, String var4) {
         this.testApi = var1;
         this.endpoint = var2;
         this.object = var3;
         this.xml = var4;
      }

      public TestNetSuiteQueryApi.Result call() throws Exception {
         TestQueryController.QueryRunner.Callback var1 = new TestQueryController.QueryRunner.Callback();
         this.testApi.testQuery(this.endpoint, this.object, this.xml, var1);
         return var1.awaitResult().orNull();
      }

      private static class Callback extends ResultProducingServerCallback<TestNetSuiteQueryApi.Result> implements TestNetSuiteQueryCallback {
         private Callback() {
         }

         @Override
         public void testComplete(TestNetSuiteQueryApi.Result var1) {
            this.set(var1);
         }
      }
   }

   private class ResultDisplayer implements Receiver<TestNetSuiteQueryApi.Result> {
      private Future<?> job;
      private boolean done;

      private ResultDisplayer() {
      }

      public synchronized void setJob(Future<?> var1) {
         if (this.done) {
            TestQueryController.this.runningJobs.jobDone(var1);
         } else {
            this.job = var1;
         }
      }

      public void handle(TestNetSuiteQueryApi.Result var1) {
         synchronized (this) {
            this.done = true;
            if (this.job != null) {
               TestQueryController.this.runningJobs.jobDone(this.job);
            }
         }

         if (var1 != null) {
            TestQueryResultDisplayer.showResult(var1);
         }
      }
   }

   private class TestQueryAction extends AbstractAction {
      public TestQueryAction() {
         super("&Test Query", ClientIcons.VALIDATE_16);
         this.putValue("KongaMediumIconKey", ClientIcons.VALIDATE_24);
         this.putValue("SwingLargeIconKey", ClientIcons.VALIDATE_24);
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (TestQueryController.this.endpoint != null && TestQueryController.this.netsuiteObject != null) {
                  TestQueryController.this.testQuery();
               }
            }
         });
      }
   }
}
