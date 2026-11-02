package com.etlforge.integration.client.ui.sfdc.connector;

import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.core.action.EntityPageSaveGate;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.ConnectionResult;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnectionTester;
import org.etlforge.system.callback.Receiver;

public final class TestSalesforceConnectionAction extends AbstractSingleActiveObjectAction<SalesforceConnector> {
   public static final String ID = "action.ic.TestSalesforceConnection";
   private final ApplicationWindow window;
   private final SalesforceConnectionTester connectionTester;
   private EntityPageSaveGate saveGate;

   public TestSalesforceConnectionAction(ApplicationWindow var1, SalesforceConnectionTester var2) {
      super(
         "action.ic.TestSalesforceConnection",
         var1.getActiveObjectService(),
         "&Test Salesforce Login",
         ClientIcons.TEST_CONNECTION_16,
         SalesforceConnector.class
      );
      this.window = var1;
      this.connectionTester = var2;
      this.putValue("ShortDescription", "Tests the Salesforce connection");
      this.putValue("KongaMediumIconKey", ClientIcons.TEST_CONNECTION_24);
      this.putValue("SwingLargeIconKey", ClientIcons.TEST_CONNECTION_32);
   }

   public void setSaveGate(EntityPageSaveGate var1) {
      this.saveGate = var1;
   }

   protected void runOnObject(final SalesforceConnector var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestSalesforceConnectionAction.this.testConnection(var1);
         }
      });
   }

   private void testConnection(SalesforceConnector var1) {
      Runnable var2 = this.getJob(var1);
      if (this.saveGate == null) {
         var2.run();
      } else {
         this.saveGate.runInCallingThread(var1, var2);
      }
   }

   private Runnable getJob(final SalesforceConnector var1) {
      return new Runnable() {
         @Override
         public void run() {
            TestConnectionJob var1x = TestSalesforceConnectionAction.this.createWorker(var1);
            var1x.submit();
         }
      };
   }

   private TestConnectionJob createWorker(SalesforceConnector var1) {
      OrgIdentifier var2 = var1.getOrgIdentifier();
      TestConnectionJob var3 = new TestConnectionJob(this.connectionTester, var2);
      TestSalesforceConnectionAction.ResultHandler var4 = this.createResultHandler(var1);
      var3.setResultReceiver(var4);
      var3.setUncaughtExceptionHandler(LoggingExceptionHandler.attention(this.getClass()));
      var3.setUseProgressDialog(0);
      return var3;
   }

   private TestSalesforceConnectionAction.ResultHandler createResultHandler(SalesforceConnector var1) {
      DefaultTestConnectionResultDisplayer var2 = new DefaultTestConnectionResultDisplayer(this.window.getFrame());
      return new TestSalesforceConnectionAction.ResultHandler(var1, var2);
   }

   private static class ResultHandler implements Receiver<ConnectionResult> {
      private final SalesforceConnector connector;
      private final Receiver<ConnectionResult> resultDisplayer;

      public ResultHandler(SalesforceConnector var1, Receiver<ConnectionResult> var2) {
         this.connector = var1;
         this.resultDisplayer = var2;
      }

      public void handle(ConnectionResult var1) {
         try {
            this.updateConnector(var1);
         } finally {
            this.resultDisplayer.handle(var1);
         }
      }

      private void updateConnector(ConnectionResult var1) {
         boolean var2 = this.connector.updateConnectionStatus(var1);
         if (var2) {
            this.saveConnector();
         }
      }

      private void saveConnector() {
         try {
            this.connector.save();
         } catch (InterchangeSavingException var2) {
            ErrorLog.log(this.getClass(), "Failed to save the updated connection status for the Salesforce Org " + this.connector.getName(), var2);
         }
      }
   }
}
