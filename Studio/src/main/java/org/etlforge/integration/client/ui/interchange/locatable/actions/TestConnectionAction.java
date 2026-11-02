package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.core.action.EntityPageSaveGate;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTesterFactory;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;

public final class TestConnectionAction extends AbstractSingleActiveObjectAction<Locatable> {
   public static final String ID = "action.ic.TestConnection";
   private final ApplicationWindow appWin;
   private final ConnectionTesterFactory sourceFactory;
   private final ConnectionTesterFactory targetFactory;
   private final EntityPageSaveGate saveGate;

   public TestConnectionAction(ApplicationWindow var1, ConnectionTesterFactory var2, ConnectionTesterFactory var3, EntityPageSaveGate var4) {
      super(var1.getActiveObjectService(), "action.ic.TestConnection", LocatableActionDictionary.DICTIONARY, Locatable.class);
      this.appWin = var1;
      this.sourceFactory = var2;
      this.targetFactory = var3;
      this.saveGate = var4;
   }

   protected void runOnObject(final Locatable var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            org.etlforge.integration.client.server.ServerConnectionService connection=org.etlforge.application.Application.getBean("ws.config.Cpp",org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration.class).getServerManager();
            if(!checkConnected(connection,()->org.etlforge.application.ui.NonBlockingAlert.info(TestConnectionAction.this.appWin.getFrame(),org.etlforge.integration.client.server.ServerConnectionService.NOT_CONNECTED_MESSAGE,"Test Connection")))return;
            IntegrationEntity var1x = (IntegrationEntity)var1;
            Runnable var2 = TestConnectionAction.this.getTestJob(var1x);
            TestConnectionAction.this.saveGate.runInCallingThread(var1x, var2);
         }
      });
   }

   static boolean checkConnected(org.etlforge.integration.client.server.ServerConnectionService connection,Runnable notice) {
      if(connection.getConnectionStatus()==org.etlforge.integration.client.server.ConnectionStatus.CONNECTED)return true;
      notice.run();return false;
   }

   private Runnable getTestJob(final IntegrationEntity var1) {
      return new Runnable() {
         @Override
         public void run() {
            ConnectionTester var1x = TestConnectionAction.this.getTester(var1);
            TestConnectionResultDisplayer var2 = new TestConnectionResultDisplayer(TestConnectionAction.this.appWin.getFrame());
            TestConnectionJob var3 = new TestConnectionJob(var1, var1x, var2);
            ManagedProject var4 = ProjectUtils.getManagedProject(var1);
            if (var4 != null) {
               var3.setHistory(var4.getTestHistory().getTestConnectionHistory());
            }

            var3.setUseProgressDialog(0);
            var3.submit();
         }
      };
   }

   private ConnectionTester getTester(IntegrationEntity var1) {
      return var1 instanceof Source ? this.sourceFactory.getConnectionTester(var1) : this.targetFactory.getConnectionTester(var1);
   }
}
