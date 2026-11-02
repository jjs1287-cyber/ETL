package org.etlforge.integration.client.ui.connector;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.test.TestConnectionResult;

public final class TestConnectionJob extends UiJob {
   private final ConnectorEndpoint endpoint;
   private final ConnectorConnectionTesterFactory testFactory;

   public TestConnectionJob(ConnectorEndpoint var1, ConnectorConnectionTesterFactory var2) {
      super("Testing the connection to " + var1.getName());
      this.endpoint = var1;
      this.testFactory = var2;
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(LoggingExceptionHandler.attention(TestConnectionJob.class, "An unexpected error occurred."));
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Sending connection parameters to the server");
      ConnectionTester var1 = this.testFactory.getConnectionTester(this.endpoint);
      TestConnectionResult var2 = var1.testConnection();
      if (var2 != null) {
         if (var2.isSuccess()) {
            NonBlockingAlert.info("Connection successful.", "Success");
         } else {
            ErrorLog.attention(this.getClass(), "The connection test failed.", new Exception(var2.getDetails()));
         }
      }
   }
}
