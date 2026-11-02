package org.etlforge.integration.client.ui.interchange.locatable.actions;

import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.test.EmptyTestConnectionHistory;
import org.etlforge.integration.data.test.TestConnectionHistory;
import org.etlforge.integration.data.test.TestConnectionResult;

final class TestConnectionJob extends UiJob {
   private final IntegrationEntity entity;
   private final ConnectionTester tester;
   private final TestConnectionResultDisplayer resultDisplayer;
   private volatile TestConnectionHistory history = new EmptyTestConnectionHistory();

   public TestConnectionJob(IntegrationEntity var1, ConnectionTester var2, TestConnectionResultDisplayer var3) {
      super("Testing the connection to \"" + var1.getName() + "\"");
      this.entity = var1;
      this.tester = var2;
      this.resultDisplayer = var3;
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(LoggingExceptionHandler.attention(TestConnectionJob.class, "An unexpected error occurred."));
   }

   public void setHistory(TestConnectionHistory var1) {
      this.history = var1;
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Testing the connection");
      TestConnectionResult var1 = this.tester.testConnection();
      if (var1 != null) {
         this.resultDisplayer.showResult(this.entity, var1);
         this.history.add(var1);
      }
   }
}
