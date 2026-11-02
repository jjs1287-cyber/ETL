package org.etlforge.integration.client.ui.connector;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.core.action.EntityPageSaveGate;
import org.etlforge.integration.data.connector.ConnectorEndpoint;

public final class TestConnectionAction extends AbstractSingleActiveObjectAction<ConnectorEndpoint> {
   public static final String ID = "";
   private final ConnectorConnectionTesterFactory testFactory;
   private final EntityPageSaveGate saveGate;

   public TestConnectionAction(ApplicationWindow var1, ConnectorConnectionTesterFactory var2, EntityPageSaveGate var3) {
      super("", var1.getActiveObjectService(), "Test Connection", ClientIcons.TEST_CONNECTION_16, ConnectorEndpoint.class);
      this.testFactory = var2;
      this.saveGate = var3;
      this.putValue("KongaMediumIconKey", ClientIcons.TEST_CONNECTION_24);
      this.putValue("SwingLargeIconKey", ClientIcons.TEST_CONNECTION_32);
   }

   protected void runOnObject(final ConnectorEndpoint var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestConnectionAction.this.startTest(var1);
         }
      });
   }

   private void startTest(final ConnectorEndpoint var1) {
      this.saveGate.runInBackgroundThread(var1, new Runnable() {
         @Override
         public void run() {
            TestConnectionJob var1x = new TestConnectionJob(var1, TestConnectionAction.this.testFactory);
            var1x.setUseProgressDialog(0);
            var1x.submit();
         }
      });
   }
}
