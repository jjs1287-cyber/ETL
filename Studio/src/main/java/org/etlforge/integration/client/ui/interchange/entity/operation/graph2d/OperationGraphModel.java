package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.event.OperationTypeChangedEvent;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.NodeFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.EmailWrapper;
import org.etlforge.integration.data.entity.operation.flow.FlowChangeEvent;
import org.etlforge.integration.data.entity.operation.flow.FlowChangeProcessor;
import org.etlforge.integration.data.entity.operation.flow.FlowRebuiltChange;
import org.etlforge.integration.data.entity.operation.flow.NewOperationPipelineChange;
import org.etlforge.integration.data.entity.operation.flow.NodeInsertedChange;
import org.etlforge.integration.data.entity.operation.flow.NodeRemovedChange;
import org.etlforge.integration.data.entity.operation.flow.OperationActivityWrapper;
import org.etlforge.integration.data.entity.operation.flow.OperationFlow;
import org.etlforge.integration.data.entity.operation.flow.OperationFlowListener;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.Route;
import org.etlforge.integration.data.entity.operation.flow.RouteInsertedChange;
import org.etlforge.integration.data.entity.operation.flow.RoutePredicates;
import org.etlforge.integration.data.entity.operation.flow.RouteRemovedChange;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineAdapter;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityInsertedEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityRemovedEvent;
import org.etlforge.system.Disposable;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.model.GraphModel;
import org.etlforge.ui.graph.model.GraphModelEventSupport;
import org.etlforge.ui.graph.model.GraphModelListener;
import org.etlforge.ui.graph.model.NodesInsertedEvent;
import org.etlforge.ui.graph.node.AbstractGraphNode;

public final class OperationGraphModel implements GraphModel, Disposable {
   private final NodeFactory nodeFactory;
   private final OperationFlow flow;
   private final OperationGraphModel.FlowListener flowListener;
   private OperationWrapperNode startNode;
   private Map<OperationWrapper, OperationWrapperNode> operationNodes;
   private Map<EmailWrapper, EmailWrapperNode> emailNodes;
   private Map<PipelineActivity, PipelineActivityNode> pipelineActivityNodes;
   private final GraphModelEventSupport eventSupport;

   public OperationGraphModel(Operation var1, NodeFactory var2) {
      this(new OperationFlow(var1), var2);
   }

   public OperationGraphModel(OperationFlow var1, NodeFactory var2) {
      this.flow = var1;
      this.nodeFactory = var2;
      this.flowListener = new OperationGraphModel.FlowListener();
      var1.addFlowListener(this.flowListener);
      var1.addPipelineListener(new OperationGraphModel.PipelineListener());
      this.eventSupport = new GraphModelEventSupport(this);
      this.build();
   }

   private void build() {
      this.operationNodes = Maps.newHashMap();
      this.pipelineActivityNodes = Maps.newIdentityHashMap();
      OperationGraphModel.NodeBuilder var1 = new OperationGraphModel.NodeBuilder(this.flow);
      this.startNode = var1.startNode;
      this.emailNodes = var1.emailNodes;
      this.rebuildScriptSourceEdges();
   }

   public OperationFlow getModeledFlow() {
      return this.flow;
   }

   public OperationWrapperNode getStartNode() {
      return this.startNode;
   }

   public Operation getMainOperation() {
      return this.startNode.getDataObject().getActivity();
   }

   public List<OperationWrapperNode> getOperationNodes() {
      return Lists.newArrayList(this.operationNodes.values());
   }

   public List<EmailWrapperNode> getEmailNodes() {
      return Lists.newArrayList(this.emailNodes.values());
   }

   public OperationWrapperNode getOperationNode(Operation var1) {
      for (Entry var3 : this.operationNodes.entrySet()) {
         if (((OperationWrapper)var3.getKey()).getActivity() == var1) {
            return (OperationWrapperNode)var3.getValue();
         }
      }

      return null;
   }

   public EmailWrapperNode getEmailNode(EmailMessage var1) {
      for (Entry var3 : this.emailNodes.entrySet()) {
         if (((EmailWrapper)var3.getKey()).getActivity() == var1) {
            return (EmailWrapperNode)var3.getValue();
         }
      }

      return null;
   }

   @Override
   public boolean contains(Object var1) {
      if (var1 instanceof OperationWrapperNode) {
         return this.operationNodes.containsKey(((OperationWrapperNode)var1).getDataObject());
      } else if (var1 instanceof EmailWrapperNode) {
         return this.emailNodes.containsKey(((EmailWrapperNode)var1).getDataObject());
      } else {
         return var1 instanceof AbstractGraphNode ? this.contains(((AbstractGraphNode)var1).getParent()) : false;
      }
   }

   public List<AbstractGraphNode> getNodes() {
      ArrayList var1 = Lists.newArrayList();

      for (OperationWrapperNode var3 : this.operationNodes.values()) {
         var1.add(var3);
         var1.addAll(var3.getChildren());
      }

      var1.addAll(this.emailNodes.values());
      return var1;
   }

   public List<? extends GraphEdge> getIncomingEdges(Object var1) {
      return this.contains(var1) ? ((AbstractGraphNode)var1).getIncomingRoutes() : null;
   }

   public List<? extends GraphEdge> getOutgoingEdges(Object var1) {
      return this.contains(var1) ? ((AbstractGraphNode)var1).getOutgoingRoutes() : null;
   }

   public List<? extends GraphEdge> getEdges() {
      HashSet var1 = new HashSet();

      for (AbstractGraphNode var3 : this.getNodes()) {
         var1.addAll(var3.getOutgoingRoutes());
      }

      return Lists.newArrayList(var1);
   }

   @Override
   public boolean isGroup(Object var1) {
      return var1 instanceof OperationWrapperNode ? this.operationNodes.containsKey(((OperationWrapperNode)var1).getDataObject()) : false;
   }

   public AbstractGraphNode getParentOf(Object var1) {
      return var1 instanceof AbstractGraphNode ? ((AbstractGraphNode)var1).getParent() : null;
   }

   @Override
   public void dispose() {
      this.flow.dispose();
   }

   @Override
   public void addModelListener(GraphModelListener var1) {
      this.eventSupport.addListener(var1);
   }

   @Override
   public void removeModelListener(GraphModelListener var1) {
      this.eventSupport.removeListener(var1);
   }

   private void rebuild() {
      this.build();
      this.eventSupport.fireModelChanged();
   }

   private OperationWrapperNode createOperationNode(OperationWrapper var1) {
      OperationWrapperNode var2 = new OperationWrapperNode(var1);
      this.operationNodes.put(var1, var2);
      this.buildPipelineNodes(var2);
      return var2;
   }

   private void buildPipelineNodes(OperationWrapperNode var1) {
      OperationPipeline var2 = var1.getDataObject().getPipeline();
      PipelineActivityNode var3 = null;

      for (PipelineActivity var5 : var2.getActivities()) {
         PipelineActivityNode var6 = this.nodeFactory.createNode(var5, var1);
         if (var3 != null) {
            var3.addOutgoingRoute(new PipelineActivityEdge(var3, var6));
         }

         var3 = var6;
         this.pipelineActivityNodes.put(var5, var6);
      }
   }

   private void rebuildScriptSourceEdges() {
      for (OperationWrapperNode var2 : this.operationNodes.values()) {
         this.rebuildScriptSourceEdges(var2);
      }
   }

   private void rebuildScriptSourceEdges(OperationWrapperNode var1) {
      for (PipelineActivityNode var3 : var1.getChildren()) {
         var3.removeScriptSourceEdges();
      }

      OperationWrapper var8 = var1.getDataObject();

      for (Route var4 : Iterables.filter(var8.getOutgoingRoutes(), RoutePredicates.FROM_SCRIPT_SOURCE)) {
         PipelineActivityNode var5 = var1.getChildFor(var4.getSourceActivity());
         if (var5 != null && var8.getPipeline().contains(var5.getDataObject())) {
            OperationWrapperNode var6 = this.operationNodes.get(var4.getTo());
            ScriptSourceEdge var7 = new ScriptSourceEdge(var5, var6);
            var5.addOutgoingRoute(var7);
         }
      }
   }

   private class FlowListener implements OperationFlowListener, FlowChangeProcessor {
      private FlowListener() {
      }

      @Override
      public void flowChanged(FlowChangeEvent var1) {
         var1.dispatchTo(this);
      }

      @Override
      public void process(NewOperationPipelineChange var1) {
         OperationWrapper var2 = var1.getOperationWrapper();
         OperationWrapperNode var3 = OperationGraphModel.this.operationNodes.get(var2);
         List var4 = var3.getChildren();
         var3.removeAllChildren();

         for (Object __etlItem251 : var4) {
            PipelineActivityNode var6 = (PipelineActivityNode) __etlItem251;
            OperationGraphModel.this.pipelineActivityNodes.remove(var6.getDataObject());
         }

         OperationGraphModel.this.buildPipelineNodes(var3);
         OperationGraphModel.this.eventSupport.fireModelChanged(new OperationTypeChangedEvent(OperationGraphModel.this, var3, var4));
      }

      @Override
      public void process(NodeInsertedChange var1) {
         OperationWrapperNode var2 = null;
         if (var1.getPreviousNode() instanceof OperationWrapper) {
            var2 = OperationGraphModel.this.getOperationNode(((OperationWrapper)var1.getPreviousNode()).getActivity());
         }

         OperationActivityWrapper var3 = var1.getInsertedNode();
         if (var3 instanceof OperationWrapper) {
            this.insertOperationNode((OperationWrapper)var3, var2);
         } else if (var3 instanceof EmailWrapper) {
            this.insertEmailNode((EmailWrapper)var3, var2);
         }
      }

      private void insertEmailNode(EmailWrapper var1, OperationWrapperNode var2) {
         EmailWrapperNode var3 = new EmailWrapperNode(var1);
         OperationGraphModel.this.emailNodes.put(var3.getDataObject(), var3);
         OperationGraphModel.this.eventSupport.fireNodesInserted(new NodesInsertedEvent(OperationGraphModel.this, var2, null, var3));
      }

      private void insertOperationNode(OperationWrapper var1, OperationWrapperNode var2) {
         OperationWrapperNode var3 = OperationGraphModel.this.createOperationNode(var1);
         ArrayList var4 = Lists.newArrayList();
         var4.add(var3);
         var4.addAll(var3.getChildren());
         OperationGraphModel.this.eventSupport.fireNodesInserted(var2, null, var4);
      }

      @Override
      public void process(NodeRemovedChange var1) {
         AbstractGraphNode var2 = this.getRemovedNode(var1);
         var2.removeAllOutgoingRoutes();

         for (GraphEdge var4 : var2.getIncomingRoutes()) {
            ((AbstractGraphNode)var4.from()).removeOutgoingRoute(var4);
         }

         var2.removeFromParent();
         OperationGraphModel.this.eventSupport.fireNodeRemoved(var2);
      }

      @Override
      public void process(FlowRebuiltChange var1) {
         OperationGraphModel.this.rebuild();
      }

      private AbstractGraphNode getRemovedNode(NodeRemovedChange var1) {
         OperationActivityWrapper var2 = var1.getRemovedNode();
         AbstractGraphNode var3 = null;
         if (var2 instanceof OperationWrapper) {
            var3 = OperationGraphModel.this.operationNodes.remove(var2);
         } else {
            var3 = OperationGraphModel.this.emailNodes.remove(var2);
         }

         assert var3 != null;
         return var3;
      }

      @Override
      public void process(RouteInsertedChange var1) {
         Route var2 = var1.getInsertedRoute();
         OperationWrapperNode var3 = OperationGraphModel.this.operationNodes.get(var2.getFrom());
         OperationActivityWrapper var4 = var2.getTo();
         AbstractGraphNode var5 = null;
         if (var4 instanceof OperationWrapper) {
            var5 = OperationGraphModel.this.operationNodes.get(var4);
         } else if (var4 instanceof EmailWrapper) {
            var5 = OperationGraphModel.this.emailNodes.get(var4);
         }

         GraphEdge var6;
         if (var2.getSourceActivity() != null) {
            var6 = this.createScriptSourceEdge(var2, var3, (OperationWrapperNode)var5);
         } else {
            var6 = this.createOperationActivityEdge(var2, var3, var5);
         }

         OperationGraphModel.this.eventSupport.fireNodesConnected(var6);
      }

      private GraphEdge createScriptSourceEdge(Route var1, OperationWrapperNode var2, OperationWrapperNode var3) {
         PipelineActivityNode var4 = var2.getChildFor(var1.getSourceActivity());
         ScriptSourceEdge var5 = new ScriptSourceEdge(var4, var3);
         var4.addOutgoingRoute(var5);
         return var5;
      }

      private GraphEdge createOperationActivityEdge(Route var1, OperationWrapperNode var2, AbstractGraphNode var3) {
         OperationActivityEdge var4 = new OperationActivityEdge(var2, var3, var1);
         var2.addOutgoingRoute(var4);
         return var4;
      }

      @Override
      public void process(RouteRemovedChange var1) {
         Route var2 = var1.getRemovedRoute();
         OperationWrapperNode var3 = OperationGraphModel.this.operationNodes.get(var2.getFrom());
         OperationActivityWrapper var4 = var2.getTo();
         AbstractGraphNode var5 = null;
         if (var4 instanceof OperationWrapper) {
            var5 = OperationGraphModel.this.operationNodes.get(var4);
         } else if (var4 instanceof EmailWrapper) {
            var5 = OperationGraphModel.this.emailNodes.get(var4);
         }

         if (var5 != null) {
            GraphEdge var6 = this.getEdgeForRemovedRoute(var2, var3, var5);
            if (var6 != null) {
               OperationGraphModel.this.eventSupport.fireNodesDisconnected(var6);
            }
         }
      }

      private GraphEdge getEdgeForRemovedRoute(Route var1, OperationWrapperNode var2, AbstractGraphNode var3) {
         GraphEdge var4 = null;
         if (var1.getSourceActivity() != null) {
            var4 = this.getEdgeForRemovedScriptSourceRoute(var1, var2, var3);
         } else {
            var4 = this.getEdgeForRemovedOperationActivityRoute(var1, var2, var3);
         }

         return var4;
      }

      private GraphEdge getEdgeForRemovedScriptSourceRoute(Route var1, OperationWrapperNode var2, AbstractGraphNode var3) {
         assert var1.getSourceActivity() != null;
         PipelineActivityNode var4 = var2.getChildFor(var1.getSourceActivity());
         if (var4 != null) {
            for (GraphEdge var6 : var4.getOutgoingRoutes()) {
               if (var6 instanceof ScriptSourceEdge && ((ScriptSourceEdge)var6).to() == var3) {
                  var4.removeOutgoingRoute(var6);
                  return var6;
               }
            }
         }

         return null;
      }

      private GraphEdge getEdgeForRemovedOperationActivityRoute(Route var1, OperationWrapperNode var2, AbstractGraphNode var3) {
         assert var1.getSourceActivity() == null;

         for (GraphEdge var5 : var2.getOutgoingRoutes()) {
            if (var5.to() == var3 && ((OperationActivityEdge)var5).getType() == var1.getType()) {
               var2.removeOutgoingRoute(var5);
               return var5;
            }
         }

         return null;
      }
   }

   private class NodeBuilder {
      private OperationWrapperNode startNode;
      private final Map<EmailWrapper, EmailWrapperNode> emailNodes = Maps.newHashMap();

      public NodeBuilder(OperationFlow var2) {
         this.build(var2.getStartPoint());
      }

      private OperationWrapperNode build(OperationWrapper var1) {
         OperationWrapperNode var2 = OperationGraphModel.this.operationNodes.get(var1);
         if (var2 != null) {
            return var2;
         }

         var2 = OperationGraphModel.this.createOperationNode(var1);
         if (this.startNode == null) {
            this.startNode = var2;
         }

         for (Route var4 : var1.getOutgoingRoutes()) {
            OperationActivityWrapper var5 = var4.getTo();
            AbstractGraphNode var6 = null;
            if (var5 instanceof OperationWrapper) {
               var6 = this.build((OperationWrapper)var5);
            } else {
               if (!(var5 instanceof EmailWrapper)) {
                  throw new RuntimeException("Unexpected OperationActivityWrapper type: " + var5.getClass());
               }

               EmailWrapperNode var7 = this.getEmailNodeFor((EmailWrapper)var5);
               this.emailNodes.put(var7.getDataObject(), var7);
               var6 = var7;
            }

            var2.addOutgoingRoute(var4, var6);
         }

         return var2;
      }

      private EmailWrapperNode getEmailNodeFor(EmailWrapper var1) {
         EmailWrapperNode var2 = this.emailNodes.get(var1);
         if (var2 == null) {
            var2 = new EmailWrapperNode(var1);
            this.emailNodes.put(var1, var2);
         }

         return var2;
      }
   }

   private class PipelineListener extends OperationPipelineAdapter {
      private PipelineListener() {
      }

      @Override
      public void activityInserted(PipelineActivityInsertedEvent var1) {
         PipelineActivityNode var2 = OperationGraphModel.this.pipelineActivityNodes.get(var1.getPrevious());
         if (var2 != null) {
            var2.removeAllOutgoingRoutes();
         }

         PipelineActivityNode var3 = var2;
         OperationWrapperNode var4 = this.getNodeForPipeline(var1.getSource());
         PipelineActivity var5 = var1.getInsertedActivity();
         PipelineActivityNode var6 = OperationGraphModel.this.nodeFactory.createNode(var5, var4);
         OperationGraphModel.this.pipelineActivityNodes.put(var5, var6);
         if (var3 != null) {
            var3.addOutgoingRoute(new PipelineActivityEdge(var3, var6));
         }

         PipelineActivityNode var7 = OperationGraphModel.this.pipelineActivityNodes.get(var1.getNext());
         if (var7 != null) {
            var6.addOutgoingRoute(new PipelineActivityEdge(var6, var7));
         }

         this.rebuildOperationNode(var4, var1.getSource());
         OperationGraphModel.this.eventSupport.fireNodeInserted(var2, var7, var6);
      }

      private void rebuildOperationNode(OperationWrapperNode var1, OperationPipeline var2) {
         var1.removeAllChildren();

         for (PipelineActivity var4 : var2.getActivities()) {
            var1.addChild(OperationGraphModel.this.pipelineActivityNodes.get(var4));
         }

         OperationGraphModel.this.rebuildScriptSourceEdges(var1);
      }

      private OperationWrapperNode getNodeForPipeline(OperationPipeline var1) {
         for (Entry var3 : OperationGraphModel.this.operationNodes.entrySet()) {
            if (((OperationWrapper)var3.getKey()).getPipeline() == var1) {
               return (OperationWrapperNode)var3.getValue();
            }
         }

         throw new RuntimeException("This model does not contain a node for the pipeline that was changed.");
      }

      @Override
      public void activityRemoved(PipelineActivityRemovedEvent var1) {
         PipelineActivity var2 = var1.getRemovedActivity();
         PipelineActivityNode var3 = OperationGraphModel.this.pipelineActivityNodes.remove(var2);
         var3.removeAllOutgoingRoutes();
         OperationWrapperNode var4 = var3.getParent();
         var3.removeFromParent();
         this.rebuildEdges(var4, var1.getSource());
         OperationGraphModel.this.eventSupport.fireNodeRemoved(var3);
      }

      private void rebuildEdges(OperationWrapperNode var1, OperationPipeline var2) {
         List var3 = var2.getActivities();
         PipelineActivityNode var4 = null;
         int var5 = var3.size();
         if (var5 == 1) {
            var4 = OperationGraphModel.this.pipelineActivityNodes.get(var3.get(0));
         } else if (var5 > 1) {
            for (int var6 = 0; var6 < var5 - 1; var6++) {
               PipelineActivityNode var7 = OperationGraphModel.this.pipelineActivityNodes.get(var3.get(var6));
               PipelineActivityNode var8 = OperationGraphModel.this.pipelineActivityNodes.get(var3.get(var6 + 1));
               var7.removeAllOutgoingRoutes();
               var7.addOutgoingRoute(new PipelineActivityEdge(var7, var8));
               var4 = var8;
            }
         }

         if (var4 != null) {
            var4.removeAllOutgoingRoutes();
         }

         OperationGraphModel.this.rebuildScriptSourceEdges(var1);
      }

      @Override
      public void pipelineChanged(OperationPipelineEvent var1) {
      }
   }
}
