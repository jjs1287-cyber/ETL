package org.etlforge.integration.client.ui.interchange.testconnection;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.functors.EntityIdCollectionFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.DottedTextLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class TestAllConnectionsViewImpl extends AbstractWindowSectionContent implements TestAllConnectionsView {
   private final AllEndpointsTester tester;
   private IntegrationProject project;
   private final TestAllConnectionsListener testListener = new TestAllConnectionsViewImpl.TestListener();
   private final TestAllConnectionsResultTable resultTable;
   private final TestAllConnectionsViewImpl.StartTestAction startAction;
   private final TestAllConnectionsViewImpl.RetestFailedConnectionsAction retestAction;
   private final TestAllConnectionsViewImpl.StopTestAction stopAction;
   private final DottedTextLabel statusPanel;
   private final ShowFailedConnectionsOnlySwitch failedConnectionsSwitch;

   public TestAllConnectionsViewImpl(WindowSection var1, IntegrationProject var2, AllEndpointsTester var3) {
      super(ID, var1);
      this.project = var2;
      this.tester = var3;
      var3.addTestListener(this.testListener);
      this.resultTable = new TestAllConnectionsResultTable(var2.getItemLookup());
      this.startAction = new TestAllConnectionsViewImpl.StartTestAction();
      this.retestAction = new TestAllConnectionsViewImpl.RetestFailedConnectionsAction();
      this.stopAction = new TestAllConnectionsViewImpl.StopTestAction();
      this.statusPanel = new DottedTextLabel();
      this.statusPanel.setVisible(false);
      this.failedConnectionsSwitch = new ShowFailedConnectionsOnlySwitch(this.resultTable);
   }

   @Override
   public void startTest() {
      this.tester.testAllEndpoints(this.project);
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 2);
      var2.withBackground(Colors.NEUTRAL_BACKGROUND);
      var2.northToSouth(this.getTitlePanel(), this.resultTable, this.layoutSouthPart());
      BorderLayoutBuilder.from(var1).center(var2);
   }

   private JComponent layoutSouthPart() {
      return Horizontal.box().nonOpaque().addAllWithSpace(10, this.failedConnectionsSwitch, this.retestAction).container();
   }

   @Override
   public String getTitle() {
      return "Test All Connections";
   }

   @Override
   protected JComponent getAdditionalTitleComponent() {
      return InvisiblePanel.of(this.statusPanel);
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.TEST_CONNECTION_16;
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      if (super.removeFromWindow(var1)) {
         this.stopTest();
         this.tester.removeTestListener(this.testListener);
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      var1.add(createToolBarButton(this.startAction));
      var1.add(createToolBarButton(this.stopAction));
      var1.add(createToolBarButton(this.resultTable.getViewDetailsAction()));
      var1.add(createToolBarButton(this.resultTable.getOpenEntityAction()));
      var1.addSeparator();
      super.populateToolBar(var1);
   }

   private void stopTest() {
      this.tester.stop();
   }

   @Override
   public void setProject(final IntegrationProject var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            TestAllConnectionsViewImpl.this.stopTest();
            TestAllConnectionsViewImpl.this.project = var1;
            TestAllConnectionsViewImpl.this.resultTable.removeAll();
            if (var1 != null) {
               TestAllConnectionsViewImpl.this.resultTable.setNewEntityLookup(var1.getItemLookup());
               TestAllConnectionsViewImpl.this.startAction.setEnabled(true);
            } else {
               TestAllConnectionsViewImpl.this.startAction.setEnabled(false);
            }
         }
      });
   }

   private class RetestFailedConnectionsAction extends AbstractAction {
      public RetestFailedConnectionsAction() {
         super("Retest", CommonIcons.ERROR_16);
         this.putValue("ShortDescription", "Retest failed connections");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RetestFailedConnectionsAction.this.retest();
            }
         });
      }

      private void retest() {
         Set var1 = TestAllConnectionsViewImpl.this.resultTable.getFailedConnections();
         if (var1.isEmpty()) {
            Alert.info(TestAllConnectionsViewImpl.this.getWindow().getFrame(), "No failed connections to re-test.");
         } else {
            EntityIdCollectionFilter var2 = new EntityIdCollectionFilter(var1);
            TestAllConnectionsViewImpl.this.tester.setFilter(var2);
            TestAllConnectionsViewImpl.this.tester.testAllEndpoints(TestAllConnectionsViewImpl.this.project);
         }
      }
   }

   private class StartTestAction extends AbstractAction {
      public StartTestAction() {
         super(null, ApplicationResources.ICONS.getIcon("Start.16"));
         this.putValue("ShortDescription", "Restart the connection tests");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TestAllConnectionsViewImpl.this.startTest();
      }
   }

   private class StopTestAction extends AbstractAction {
      public StopTestAction() {
         super(null, ApplicationResources.ICONS.getIcon("Stop.16"));
         this.putValue("ShortDescription", "Stop the connection tests");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TestAllConnectionsViewImpl.this.stopTest();
      }
   }

   private class TestListener implements TestAllConnectionsListener {
      private TestListener() {
      }

      @Override
      public void connectionWasTested(TestConnectionResult var1) {
         TestAllConnectionsViewImpl.this.resultTable.addResult(var1);
      }

      @Override
      public void testDone() {
         this.testStopped();
      }

      @Override
      public void testStarted() {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               TestAllConnectionsViewImpl.this.resultTable.removeAll();
               TestAllConnectionsViewImpl.this.startAction.setEnabled(false);
               TestAllConnectionsViewImpl.this.stopAction.setEnabled(true);
               TestAllConnectionsViewImpl.this.retestAction.setEnabled(false);
               TestAllConnectionsViewImpl.this.statusPanel.setVisible(true);
               TestAllConnectionsViewImpl.this.statusPanel.setUpdateAutomatically(150);
            }
         });
      }

      @Override
      public void testStopped() {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               TestAllConnectionsViewImpl.this.startAction.setEnabled(TestAllConnectionsViewImpl.this.project != null);
               TestAllConnectionsViewImpl.this.stopAction.setEnabled(false);
               TestAllConnectionsViewImpl.this.retestAction.setEnabled(TestAllConnectionsViewImpl.this.resultTable.hasFailedConnections());
               TestAllConnectionsViewImpl.this.statusPanel.setText("");
               TestAllConnectionsViewImpl.this.statusPanel.setVisible(false);
               TestAllConnectionsViewImpl.this.statusPanel.stopAutomaticUpdate();
               if (TestAllConnectionsViewImpl.this.tester != null) {
                  TestAllConnectionsViewImpl.this.tester.setFilter(EntityFilters.ACCEPTS_ALL);
               }
            }
         });
      }

      @Override
      public void progressUpdate(final String var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               TestAllConnectionsViewImpl.this.statusPanel.setText(var1);
            }
         });
      }
   }
}
