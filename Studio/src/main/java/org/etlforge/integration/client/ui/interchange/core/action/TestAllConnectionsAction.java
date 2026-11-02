package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.AddContentCallback;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;
import org.etlforge.integration.client.ui.interchange.testconnection.AllEndpointsTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTesterFactory;
import org.etlforge.integration.client.ui.interchange.testconnection.TestAllConnectionsView;
import org.etlforge.integration.client.ui.interchange.testconnection.TestAllConnectionsViewFactory;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.test.TestConnectionHistory;
import org.etlforge.ui.wait.WaitLock;

public final class TestAllConnectionsAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.TestAllConnections";
   private final ApplicationWindow appWin;
   private final ImmutableList<ConnectionTesterFactory> testFactories;

   public TestAllConnectionsAction(
      ApplicationWindow var1, CurrentProjectService var2, Iterable<ConnectionTesterFactory> var3, Iterable<ConnectionTesterFactory> var4
   ) {
      super(var2, "action.ic.TestAllConnections", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.testFactories = KongaListUtils.concatenateToImmutableList(var3, var4);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestAllConnectionsAction.this.openTestConnectionView();
         }
      });
   }

   private void openTestConnectionView() {
      WaitLock var1 = this.appWin.startWait();
      IntegrationProject var2 = this.getProject();
      AllEndpointsTester var3 = this.createTester(this.getManagedProject());
      TestAllConnectionsViewFactory var4 = new TestAllConnectionsViewFactory(var2, var3);
      this.appWin
         .displayContent(
            var4, ApplicationWindow.SectionPosition.SOUTH, new TestAllConnectionsAction.ContentUpdater(var2), new TestAllConnectionsAction.Callback(var1)
         );
   }

   private AllEndpointsTester createTester(ManagedProject var1) {
      AllEndpointsTester var2 = new AllEndpointsTester(this.testFactories);
      TestConnectionHistory var3 = var1.getTestHistory().getTestConnectionHistory();
      var2.setTestHistory(var3);
      return var2;
   }

   private class Callback implements AddContentCallback {
      private final WaitLock waitLock;

      public Callback(WaitLock var2) {
         this.waitLock = var2;
      }

      @Override
      public void contentAdded(WindowSection var1, WindowSectionContent var2) {
         this.waitLock.release();
         TestAllConnectionsView var3 = (TestAllConnectionsView)var2;
         var3.addResource(TestAllConnectionsAction.this.new ProjectListening(var3));
         var3.startTest();
      }

      @Override
      public void cancelled() {
         this.waitLock.release();
      }

      @Override
      public void failed(Exception var1) {
         this.waitLock.release();
         ErrorLog.attention(TestAllConnectionsAction.class, "An error occurred: " + var1.getMessage(), var1);
      }
   }

   private static class ContentUpdater implements WindowSectionContentUpdater {
      private final IntegrationProject project;

      public ContentUpdater(IntegrationProject var1) {
         this.project = var1;
      }

      @Override
      public void update(WindowSectionContent var1) {
         TestAllConnectionsView var2 = (TestAllConnectionsView)var1;
         var2.setProject(this.project);
         var2.startTest();
      }
   }

   private class ProjectListening extends CurrentProjectListenerResource {
      private final TestAllConnectionsView view;

      public ProjectListening(TestAllConnectionsView var2) {
         super(TestAllConnectionsAction.this.getCurrentProjectService());
         this.view = var2;
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         this.view.setProject(var1.getProject());
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         this.view.setProject(null);
      }
   }
}
