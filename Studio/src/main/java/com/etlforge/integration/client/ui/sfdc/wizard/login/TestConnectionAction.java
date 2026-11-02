package com.etlforge.integration.client.ui.sfdc.wizard.login;

import com.etlforge.integration.client.ui.sfdc.connector.DefaultTestConnectionResultDisplayer;
import com.etlforge.integration.client.ui.sfdc.connector.TestConnectionJob;
import com.etlforge.integration.client.ui.sfdc.connector.page.SalesforceConnectorPanel;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SalesforceConnectionTester;

final class TestConnectionAction extends AbstractAction {
   private final SalesforceConnectionTester tester;
   private final SalesforceConnectorPanel panel;

   public TestConnectionAction(SalesforceConnectionFactory var1, SalesforceConnectorPanel var2) {
      super("&Test Salesforce Login", ClientIcons.TEST_CONNECTION_16);
      this.tester = new SalesforceConnectionTester(var1);
      this.panel = var2;
      this.putValue("KongaMediumIconKey", ClientIcons.TEST_CONNECTION_24);
      this.putValue("SwingLargeIconKey", ClientIcons.TEST_CONNECTION_32);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestConnectionAction.this.testConnection();
         }
      });
   }

   private void testConnection() {
      TestConnectionJob var1 = this.createWorker();
      var1.submit();
   }

   private TestConnectionJob createWorker() {
      OrgIdentifier var1 = this.panel.getOrgIdentifier();
      TestConnectionJob var2 = new TestConnectionJob(this.tester, var1);
      var2.setResultReceiver(new DefaultTestConnectionResultDisplayer(this.panel.getUi()));
      var2.setUseProgressDialog(0);
      return var2;
   }
}
