package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderEvent;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListeners;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout.NodeExpansionMemento;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout.OperationGraphLayout;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.NodeFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.ui.graph.GraphSelectionEvent;
import org.etlforge.ui.graph.GraphSelectionListener;
import org.etlforge.ui.graph.model.GraphModel;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;

public final class OperationGraph2D extends Graph2D implements ActiveObjectProvider {
   private final OperationGraphConfig graphConfig;
   private final OperationGraph2D.ActiveObjectHelper activeObjectHelper = new OperationGraph2D.ActiveObjectHelper();
   private final ActiveObjectProviderListeners activeObjectListeners = new ActiveObjectProviderListeners();
   private Preference<EdgeType> edgeTypePreference;
   private PreferenceListener<EdgeType> edgeTypePreferenceListener;

   public OperationGraph2D(Operation var1, OperationGraphConfig var2) {
      this(new OperationGraphModel(var1, new NodeFactory(var2)), var2);
   }

   public OperationGraph2D(OperationGraphModel var1, OperationGraphConfig var2) {
      super(var1, new OperationGraphNodeUiFactory(), new OperationGraphEdgeUiFactory());
      this.graphConfig = var2;
      this.getSelectionModel().addSelectionListener(this.activeObjectHelper);
   }

   public OperationGraphConfig getGraphConfig() {
      return this.graphConfig;
   }

   @Override
   public void dispose() {
      try {
         super.dispose();
      } finally {
         this.getModel().dispose();
         this.removeEdgeTypePreferenceListener();
      }
   }

   @Override
   protected GraphLayout2D createLayout() {
      return new OperationGraphLayout(this);
   }

   public OperationGraphModel getModel() {
      return (OperationGraphModel)super.getModel();
   }

   public OperationGraphLayout getLayout() {
      return (OperationGraphLayout)super.getLayout();
   }

   @Override
   public void setModel(GraphModel var1) {
      this.setModel(var1, true);
   }

   public void setModel(GraphModel var1, boolean var2) {
      if (!(var1 instanceof OperationGraphModel)) {
         throw new IllegalArgumentException("data must be an " + OperationGraphModel.class.getName());
      }

      OperationGraphModel var3 = this.getModel();
      if (var3 != var1) {
         if (var2 && var3 != null) {
            var3.dispose();
         }

         super.setModel(var1);
      }
   }

   public void selectStartOperation() {
      OperationWrapperNode var1 = this.getModel().getStartNode();
      this.getSelectionModel().setSelection(Collections.singletonList(var1));
   }

   @Override
   public List<?> getActiveObjects() {
      return this.activeObjectHelper.getActiveObjects();
   }

   @Override
   public String getProviderId() {
      return this.getClass().getName();
   }

   @Override
   public boolean isActive() {
      return true;
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectListeners.add(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectListeners.remove(var1);
   }

   public void fireActiveObjectsChanged() {
      this.activeObjectListeners.call().activeObjectsChanged(new ActiveObjectProviderEvent(this, this.getActiveObjects()));
   }

   public NodeExpansionMemento getNodeExpansionMemento() {
      return new NodeExpansionMemento(this);
   }

   public void setEdgeTypePreference(Preference<EdgeType> var1) {
      this.removeEdgeTypePreferenceListener();
      this.edgeTypePreference = var1;
      this.addEdgeTypePreferenceListener();
   }

   private void removeEdgeTypePreferenceListener() {
      if (this.edgeTypePreference != null) {
         this.edgeTypePreference.removeListener(this.edgeTypePreferenceListener);
      }
   }

   private void addEdgeTypePreferenceListener() {
      if (this.edgeTypePreference != null) {
         if (this.edgeTypePreferenceListener == null) {
            this.edgeTypePreferenceListener = new OperationGraph2D.EdgeTypePreferenceListener();
         }

         this.edgeTypePreference.addListener(this.edgeTypePreferenceListener);
      }
   }

   private class ActiveObjectHelper implements GraphSelectionListener {
      private ActiveObjectHelper() {
      }

      @Override
      public synchronized void selectionChanged(GraphSelectionEvent var1) {
         OperationGraph2D.this.fireActiveObjectsChanged();
      }

      public synchronized List<?> getActiveObjects() {
         List var1 = OperationGraph2D.this.getSelectionModel().getSelection();
         if (var1 != null && !var1.isEmpty()) {
            return this.convertSelectionToActiveObjects(var1);
         }

         Operation var2 = OperationGraph2D.this.getModel().getStartNode().getDataObject().getActivity();
         return Collections.singletonList(var2);
      }

      private List<?> convertSelectionToActiveObjects(List<Object> var1) {
         ArrayList var2 = Lists.newArrayList();

         for (Object var4 : var1) {
            if (var4 instanceof OperationWrapperNode) {
               var2.add(((OperationWrapperNode)var4).getDataObject().getActivity());
            } else if (var4 instanceof EmailWrapperNode) {
               var2.add(((EmailWrapperNode)var4).getDataObject().getActivity());
            } else if (var4 instanceof PipelineActivityNode) {
               PipelineActivity var5 = ((PipelineActivityNode)var4).getDataObject();
               Object var6 = var5.getContent();
               if (var6 instanceof IntegrationEntity) {
                  var2.add(var6);
               }
            }
         }

         return var2;
      }
   }

   private class EdgeTypePreferenceListener implements PreferenceListener<EdgeType> {
      private EdgeTypePreferenceListener() {
      }

      public void valueChanged(Preference<EdgeType> var1, EdgeType var2, EdgeType var3) {
         OperationGraph2D.this.setEdgeUiFactory(new OperationGraphEdgeUiFactory());
      }
   }
}
