package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphModel;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.UndoRedoMode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.NewOperationProcessor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphControllerListener;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs.UndoRedoModePreference;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.toolbar.OnFailureToolbarContribution;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.toolbar.OnSuccessToolbarContribution;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.toolbar.OperationGraphToolbar;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.FlowChangeEvent;
import org.etlforge.integration.data.entity.operation.flow.FlowChangeProcessor;
import org.etlforge.integration.data.entity.operation.flow.FlowRebuiltChange;
import org.etlforge.integration.data.entity.operation.flow.NewOperationPipelineChange;
import org.etlforge.integration.data.entity.operation.flow.NodeInsertedChange;
import org.etlforge.integration.data.entity.operation.flow.NodeRemovedChange;
import org.etlforge.integration.data.entity.operation.flow.OperationActivityWrapper;
import org.etlforge.integration.data.entity.operation.flow.OperationFlowListener;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.RouteInsertedChange;
import org.etlforge.integration.data.entity.operation.flow.RouteRemovedChange;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineListener;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityInsertedEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityRemovedEvent;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.widget.overlay.OverlayCornerPositioner;

public final class OperationGraphPanel extends InputPanel {
   static final String ZOOM_KEY = "graph.zoom";
   private static final boolean TOOLBAR_SUPPORTED = false;
   private final Operation operation;
   private final GraphBuilder graphBuilder;
   private final OperationGraph2D graph;
   private final OperationGraphController controller;
   private final OperationGraphPanel.Listener listener;
   private final List<OperationGraphListener> graphListeners;

   public OperationGraphPanel(Operation var1, GraphBuilder var2, OperationGraphController var3) {
      this.operation = var1;
      this.graphBuilder = var2;
      this.controller = var3;
      this.graph = var2.build();
      var3.setGraph(this.graph);
      this.graph.addPropertyChangeListener("zoomLevel", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            OperationGraphPanel.this.getUi().repaint();
         }
      });
      this.listener = new OperationGraphPanel.Listener();
      this.listener.install(this.graph.getModel(), var3);
      this.graphListeners = new CopyOnWriteArrayList<>();
      this.setLayout(new BorderLayout());
      ContainerCanvas.decorate(this.graph, this.getUi(), "Center");
   }

   public OperationGraph2D getGraph() {
      return this.graph;
   }

   public void save(OperationGraphSaver var1) {
      var1.saveGraphLogic(this.graph.getModel().getModeledFlow());
      var1.saveGraphSettings(this.graph);
   }

   public void prepareForFirstDisplay() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OperationGraphPanel.this.graph.requestFocus();
            if (OperationGraphPanel.this.operation.isTransient()) {
               NewOperationProcessor var1 = new NewOperationProcessor(OperationGraphPanel.this.controller);
               var1.process(OperationGraphPanel.this.operation);
            }
         }
      });
   }

   public void addOperationGraphListener(OperationGraphListener var1) {
      this.graphListeners.add(var1);
   }

   public void removeOperationGraphListener(OperationGraphListener var1) {
      this.graphListeners.remove(var1);
   }

   private void installToolbar() {
      OperationGraphToolbar var1 = new OperationGraphToolbar();
      var1.addContribution(new OnSuccessToolbarContribution(this.controller));
      var1.addContribution(new OnFailureToolbarContribution(this.controller));
      var1.register(this.graph);
      this.graph.getOverlayContainer().addOverlay(var1.getUi(), OverlayCornerPositioner.UPPER_RIGHT);
   }

   @Override
   public void dispose() {
      try {
         this.controller.dispose();
         this.graph.dispose();
         this.listener.dispose(this.graph.getModel());
      } finally {
         super.dispose();
      }
   }

   private class Listener
      implements PropertyChangeListener,
      OperationFlowListener,
      FlowChangeProcessor,
      OperationPipelineListener,
      OperationGraphControllerListener {
      private final UndoRedoMode undoRedoMode = UndoRedoModePreference.get();

      private Listener() {
      }

      public void install(OperationGraphModel var1, OperationGraphController var2) {
         var1.getModeledFlow().addFlowListener(this);
         var1.getModeledFlow().addPipelineListener(this);
         this.listenToOperationWrappers(var1);
         var2.addControllerListener(this);
      }

      private void listenToOperationWrappers(OperationGraphModel var1) {
         for (OperationWrapperNode var3 : var1.getOperationNodes()) {
            var3.getDataObject().addPropertyChangeListener(this);
         }
      }

      public void dispose(OperationGraphModel var1) {
         var1.getModeledFlow().removeFlowListener(this);
         var1.getModeledFlow().removePipelineListener(this);

         for (OperationWrapperNode var3 : var1.getOperationNodes()) {
            var3.getDataObject().removePropertyChangeListener(this);
         }
      }

      @Override
      public void process(NodeInsertedChange var1) {
         OperationActivityWrapper var2 = var1.getInsertedNode();
         if (var2 instanceof OperationWrapper) {
            ((OperationWrapper)var2).addPropertyChangeListener(this);
         }
      }

      @Override
      public void process(NodeRemovedChange var1) {
         OperationActivityWrapper var2 = var1.getRemovedNode();
         if (var2 instanceof OperationWrapper) {
            ((OperationWrapper)var2).removePropertyChangeListener(this);
         }
      }

      @Override
      public void process(NewOperationPipelineChange var1) {
      }

      @Override
      public void process(RouteInsertedChange var1) {
      }

      @Override
      public void process(RouteRemovedChange var1) {
      }

      @Override
      public void process(FlowRebuiltChange var1) {
         this.listenToOperationWrappers(OperationGraphPanel.this.graph.getModel());
         OperationGraphPanel.this.graphBuilder.restoreLayout(OperationGraphPanel.this.graph);

         for (OperationGraphListener var3 : OperationGraphPanel.this.graphListeners) {
            var3.graphRebuilt();
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (OperationGraphPanel.this.graph.getModel().getModeledFlow().isDirty()) {
            OperationGraphPanel.this.fireInputChanged(null, null);
         }
      }

      @Override
      public void flowChanged(FlowChangeEvent var1) {
         var1.dispatchTo(this);
      }

      @Override
      public void activityInserted(PipelineActivityInsertedEvent var1) {
         OperationGraphPanel.this.fireInputChanged(null, null);
      }

      @Override
      public void activityRemoved(PipelineActivityRemovedEvent var1) {
         OperationGraphPanel.this.fireInputChanged(null, null);
      }

      @Override
      public void pipelineChanged(OperationPipelineEvent var1) {
         OperationGraphPanel.this.fireInputChanged(null, null);
      }

      @Override
      public void activityObjectChanged(PipelineActivityEvent var1) {
      }

      @Override
      public void contentChanged(PipelineActivityEvent var1) {
         OperationGraphPanel.this.fireInputChanged(null, null);
      }

      @Override
      public void layoutWasChanged(UndoableEdit var1) {
         OperationGraphPanel.this.fireInputChanged(null, null);
         if (this.undoRedoMode != UndoRedoMode.NONE) {
            OperationGraphPanel.this.fireUndoableEditEvent(new UndoableEditEvent(OperationGraphPanel.this.graph, var1));
         }
      }

      @Override
      public void graphDataChanged(UndoableEdit var1) {
         OperationGraphPanel.this.fireUndoableEditEvent(new UndoableEditEvent(OperationGraphPanel.this.graph, var1));
      }
   }
}
