package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.application.ui.window.event.ActiveObjectProviderEvent;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutStore;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.operation.OperationMonitorPageSite;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class OperationPage extends IntegrationEntityPage implements OperationMonitorSite {
   private final OperationGraphConfig graphConfig;
   private OperationGraphController graphController;
   private OperationGraphSaver graphSaver;
   private GraphLayoutStore layoutStore;
   private OperationMonitorSite activityMonitorHelper = OperationMonitorSite.INACTIVE;

   public OperationPage(Operation var1, OperationGraphConfig var2) {
      super(new IntegrationEntityPageModel(var1));
      this.graphConfig = var2;
      this.graphController = OperationGraphController.READ_ONLY;
      this.graphSaver = OperationGraphSaver.READ_ONLY;
      this.layoutStore = GraphLayoutStore.EMPTY;
      this.addResource(new ScrollPositionSaver(this));
      this.setIncludeNameField(false);
      this.setUseScroll(false);
   }

   public void installMonitoring(OperationMonitor var1) {
      this.activityMonitorHelper = new OperationMonitorPageSite(var1, this);
   }

   public void setGraphController(OperationGraphController var1) {
      this.graphController = var1;
   }

   public OperationGraphController getGraphController() {
      return this.graphController;
   }

   public void setGraphLayoutStore(GraphLayoutStore var1) {
      this.layoutStore = var1;
   }

   GraphLayoutStore getLayoutStore() {
      return this.layoutStore;
   }

   public void setGraphSaver(OperationGraphSaver var1) {
      this.graphSaver = var1;
   }

   protected OperationPanel createDataPanel() {
      GraphBuilder var1 = new GraphBuilder(this.getObject(), this.graphConfig, this.layoutStore);
      OperationGraphPanel var2 = new OperationGraphPanel(this.getObject(), var1, this.graphController);
      OperationPanel var3 = new OperationPanel(this.getWindow(), this.getObject(), var2, this.graphSaver);
      var3.getGraph().addActiveObjectListener(new ActiveObjectProviderListener() {
         @Override
         public void activeObjectsChanged(ActiveObjectProviderEvent var1) {
            OperationPage.this.fireActiveObjectsChanged();
         }
      });
      var3.addOperationGraphListener(new OperationPage.GraphListenerImpl());
      return var3;
   }

   protected OperationPanel getDetailsPanel() {
      return (OperationPanel)super.getDetailsPanel();
   }

   public Operation getObject() {
      return (Operation)super.getObject();
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.removeContextMenuShortcut();
   }

   @Override
   protected void layoutPage(IntegrationEntityPanel var1) {
      if (this.activityMonitorHelper instanceof OperationMonitorPageSite) {
         ((OperationMonitorPageSite)this.activityMonitorHelper).layoutPage(var1.getUi(), false, false);
      } else {
         super.layoutPage(var1);
      }
   }

   @Override
   public List<?> getActiveObjects() {
      return this.getDetailsPanel().getGraph().getActiveObjects();
   }

   public OperationGraph2D getGraph() {
      return this.getDetailsPanel().getGraph();
   }

   public OperationPipeline getPipeline() {
      return this.getGraph().getModel().getStartNode().getPipeline();
   }

   @Override
   public void openActivityMonitor(long var1) {
      this.activityMonitorHelper.openActivityMonitor(var1);
   }

   @Override
   public void closeActivityMonitor() {
      this.activityMonitorHelper.closeActivityMonitor();
   }

   private class GraphListenerImpl implements OperationGraphListener {
      private GraphListenerImpl() {
      }

      @Override
      public void graphRebuilt() {
         try {
            OperationPage.this.save();
         } catch (SaveEditorException var2) {
            ErrorLog.attention(OperationPage.class, var2.getMessage(), var2);
         }

         OperationPage.this.getUndoService().discardAllEdits();
      }
   }
}
