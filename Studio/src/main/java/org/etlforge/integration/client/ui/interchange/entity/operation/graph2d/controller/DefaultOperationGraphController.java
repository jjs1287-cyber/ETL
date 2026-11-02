package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.UndoableEdit;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityContentProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphManager;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.UndoRedoMode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.OperationGraphDropHandler;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout.NodeExpansionMemento;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout.OperationGraphLayout;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationNodeController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs.UndoRedoModePreference;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.NodeExpansionEdit;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.Route;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.MutablePipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.layout.GraphLayoutEdit;
import org.etlforge.ui.graph.layout.GraphLayoutMemento;
import org.etlforge.ui.graph.model.GraphModelAdapter;
import org.etlforge.ui.graph.model.GraphModelEvent;
import org.etlforge.ui.graph.model.NodesInsertedEvent;
import org.etlforge.ui.graph.model.NodesRemovedEvent;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.graph2d.GraphController2D;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;
import org.etlforge.ui.graph2d.layout.NodeCollapsedEvent;
import org.etlforge.ui.graph2d.layout.NodeExpandedEvent;
import org.etlforge.ui.graph2d.layout.NodeExpansionListener;
import org.etlforge.ui.print.GenericPrinter;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;

public final class DefaultOperationGraphController implements OperationGraphController {
   private final ApplicationWindow appWin;
   private OperationGraph2D graph;
   private final IntegrationProject project;
   private final ProjectPersistor itemSaver;
   private final DeployFacade deployFacade;
   private final EntityExplorerSupport projectExplorer;
   private final UiEntityFactory entityFactory;
   private Action collapseAllPipelinesAction;
   private final OperationNodeController opNodeController;
   private final Map<OperationWrapperNode, PipelineAutoCompleterTrigger> autoCompleters;
   private DefaultOperationGraphController.InputHandler inputHandler;
   private final OperationTypeSetter opTypeSetter;
   private final List<OperationGraphControllerListener> controllerListeners;
   private final UndoRedoMode undoRedoMode;
   private GenericPrinter graphPrinter;
   private Action runOperationAction;
   private Action deploySelectedItemsAction;
   private Action setPermissionsAction;
   private Action assignPluginAction;
   private Action testConnectionAction;
   private Action viewDependenciesAction;
   private OperationRunnerFactory runnerFactory;

   public DefaultOperationGraphController(
      ApplicationWindow var1, IntegrationProject var2, ProjectPersistor var3, DeployFacade var4, EntityExplorerSupport var5, UiEntityFactory var6
   ) {
      this.appWin = var1;
      this.project = var2;
      this.itemSaver = var3;
      this.deployFacade = var4;
      this.projectExplorer = var5;
      this.entityFactory = var6;
      this.opNodeController = new OperationNodeControllerImpl(this);
      this.autoCompleters = Maps.newHashMap();
      this.opTypeSetter = new OperationTypeSetter(this);
      this.controllerListeners = new CopyOnWriteArrayList<>();
      this.undoRedoMode = UndoRedoModePreference.get();
   }

   @Override
   public Operation getMainOperation() {
      return this.graph == null ? null : this.graph.getModel().getMainOperation();
   }

   @Override
   public void deployMainOperation() {
      Operation var1 = this.getMainOperation();
      this.deployFacade.deploy(var1);
   }

   @Override
   public void setGraph(OperationGraph2D var1) {
      this.graph = var1;
      this.collapseAllPipelinesAction = new DefaultOperationGraphController.CollapseAllOperationsAction();
      this.inputHandler = new DefaultOperationGraphController.InputHandler(var1);
      this.graphPrinter = new GenericPrinter(var1);
      var1.getModel().addModelListener(new DefaultOperationGraphController.ModelListener());
      var1.getLayout().addNodeExpansionListener(new DefaultOperationGraphController.NodeExpansionListenerImpl());
      var1.setDropHandler(new OperationGraphDropHandler(this));
      var1.addPropertyChangeListener("model", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            DefaultOperationGraphController.this.graph.getModel().addModelListener(DefaultOperationGraphController.this.new ModelListener());
         }
      });
      this.setOperationNodeController();
      this.registerKeyBindings();
      this.installAutoCompleters();
   }

   @Override
   public void dispose() {
      this.opTypeSetter.dispose();
      this.disposeAutoCompleters();
   }

   @Override
   public ApplicationWindow getWindow() {
      return this.appWin;
   }

   @Override
   public OperationGraph2D getGraph() {
      return this.graph;
   }

   @Override
   public OperationGraphManager getGraphManager() {
      return OperationGraphManager.getManager(this.getWindow());
   }

   @Override
   public IntegrationProject getProject() {
      return this.project;
   }

   @Override
   public UiEntityFactory getEntityFactory() {
      return this.entityFactory;
   }

   @Override
   public EntityExplorerSupport getExplorerSupport() {
      return this.projectExplorer;
   }

   @Override
   public IntegrationEntityLookup getEntityLookup() {
      return this.project.getItemLookup();
   }

   @Override
   public Editor openEditor(IntegrationEntity var1) {
      return UiCompanion.of(var1).openPage();
   }

   @Override
   public OperationRunnerFactory getRunnerFactory() {
      return this.runnerFactory;
   }

   @Override
   public Action getRunOperationAction() {
      return this.runOperationAction;
   }

   public void setOperationRunnerFactory(OperationRunnerFactory var1) {
      this.runnerFactory = var1;
   }

   public void setRunOperationAction(Action var1) {
      this.runOperationAction = var1;
   }

   @Override
   public Action getDeploySelectedItemsAction() {
      return this.deploySelectedItemsAction;
   }

   public void setDeploySelectedItemsAction(Action var1) {
      this.deploySelectedItemsAction = var1;
   }

   @Override
   public Action getSetPermissionsAction() {
      return this.setPermissionsAction;
   }

   public void setSetPermissionsAction(Action var1) {
      this.setPermissionsAction = var1;
   }

   @Override
   public Action getAssignPluginAction() {
      return this.assignPluginAction;
   }

   public void setAssignPluginAction(Action var1) {
      this.assignPluginAction = var1;
   }

   @Override
   public Action getTestConnectionAction() {
      return this.testConnectionAction;
   }

   public void setTestConnectionAction(Action var1) {
      this.testConnectionAction = var1;
   }

   @Override
   public Action getViewDependenciesAction() {
      return this.viewDependenciesAction;
   }

   public void setViewDependenciesAction(Action var1) {
      this.viewDependenciesAction = var1;
   }

   @Override
   public Action getCollapseAllPipelinesAction() {
      return this.collapseAllPipelinesAction;
   }

   @Override
   public boolean isChangeAllowed(OperationWrapperNode var1) {
      Operation var2 = var1.getDataObject().getActivity();
      return this.getGraphManager().isChangeAllowed(var2, this.graph);
   }

   @Override
   public boolean isChangeAllowed(OperationWrapper var1) {
      Operation var2 = var1.getActivity();
      return this.getGraphManager().isChangeAllowed(var2, this.graph);
   }

   @Override
   public boolean isChangeAllowed(PipelineActivityNode var1) {
      Operation var2 = var1.getParent().getDataObject().getActivity();
      return this.getGraphManager().isChangeAllowed(var2, this.graph);
   }

   @Override
   public void setOperationName(OperationWrapperNode var1, String var2) {
      new OperationNameSetter(this).setName(var1, var2);
   }

   @Override
   public void setOperationType(OperationWrapperNode var1, OperationType var2) {
      this.opTypeSetter.setOperationType(var1, var2);
   }

   @Override
   public void openOptionsUi(OperationWrapperNode var1) {
      OperationOptionsUiLauncher var2 = new OperationOptionsUiLauncher(this.getWindow(), this);
      var2.showOptions(var1);
   }

   @Override
   public void setOperationOptions(OperationWrapperNode var1, OperationOptions var2) {
      new OperationOptionsSetter(this).setOptions(var1, var2);
   }

   @Override
   public boolean setSchedule(OperationWrapperNode var1, Schedule var2) {
      return new ScheduleSetter(this).setSchedule(var1, var2);
   }

   @Override
   public void setSchedulePolicy(OperationWrapperNode var1, SchedulePolicy var2) {
      new SchedulePolicySetter(this).setPolicy(var1, var2);
   }

   @Override
   public boolean setNextOperation(OperationWrapperNode var1, Operation var2, RouteType var3) {
      return new NextOperationSetter(this).setNextOperation(var1, var2, var3);
   }

   @Override
   public boolean setNextEmail(OperationWrapperNode var1, EmailMessage var2, RouteType var3) {
      return new NextEmailSetter(this).setNextEmail(var1, var2, var3);
   }

   @Override
   public void setSiblings(OperationWrapper var1, List<Operation> var2) {
      new SiblingOperationsSetter(this).setSiblings(var1, var2);
   }

   @Override
   public void deleteRoute(Route var1) {
      new RouteDeleter(this).delete(var1);
   }

   @Override
   public void renameNode(AbstractGraphNode var1) {
      RenameNodeAction var2 = new RenameNodeAction(this, var1);
      if (var2.isEnabled()) {
         ActionUtils.run(var2, this, "rename_node");
      }
   }

   @Override
   public boolean isDeletable(AbstractGraphNode var1) {
      if (var1 instanceof OperationWrapperNode) {
         OperationWrapperNode var2 = (OperationWrapperNode)var1;
         if (var2 == this.graph.getModel().getStartNode()) {
            return false;
         }

         if (Iterables.any(var2.getDataObject().getIncomingRoutes(), RouteType.SCRIPTED)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean deleteNode(AbstractGraphNode var1) {
      return new NodeDeleter(this).delete(var1);
   }

   @Override
   public <E extends IntegrationEntity> boolean setActivityNodeContent(OperationWrapper var1, MutablePipelineActivity<E> var2, E var3) {
      return new PipelineActivityContentSetter<E>(this).setContent(var1, var2, var3);
   }

   @Override
   public void clearNodeContent(PipelineActivityNode var1) {
      new PipelineActivityContentClearer(this).clearNodeContent(var1);
   }

   @Override
   public void addControllerListener(OperationGraphControllerListener var1) {
      this.controllerListeners.add(var1);
      this.inputHandler.addControllerListener(var1);
   }

   @Override
   public void removeControllerListener(OperationGraphControllerListener var1) {
      this.controllerListeners.remove(var1);
      this.inputHandler.removeControllerListener(var1);
   }

   @Override
   public void resetLayout() {
      new DefaultOperationGraphController.LayoutChange() {
         @Override
         protected void doChange(OperationGraphLayout var1) {
            var1.resetLayout();
            DefaultOperationGraphController.this.graph.ensureNodeIsVisible(DefaultOperationGraphController.this.graph.getModel().getStartNode());
         }
      };
   }

   @Override
   public void resetOperationNodeLayout(final OperationWrapperNode var1) {
      new DefaultOperationGraphController.LayoutChange() {
         @Override
         protected void doChange(OperationGraphLayout var1x) {
            var1x.resetOperationNodeLayout(var1);
         }
      };
   }

   @Override
   public boolean insertNewActivity(OperationPipeline var1, ActivityConnection var2, PipelineActivity var3) {
      return new PipelineActivityInserter(this).insertNewActivity(var1, var2, var3);
   }

   @Override
   public void selectActivity(PipelineActivityNode var1) {
      PipelineActivityType var2 = var1.getDataObject().getType();
      ActivityTypeConfig var3 = this.getGraph().getGraphConfig().getTypeConfig(var2);
      EntityContentProvider var4 = var3.getContentProvider(this, var1.getParent().getDataObject(), var1.getDataObject());
      if (var4 != null) {
         ContentProviderGate var5 = new ContentProviderGate(this, var1.getParent());
         if (var5.isOpen()) {
            var4.selectExisting();
         }
      }
   }

   @Override
   public GenericPrinter getGraphPrinter() {
      return this.graphPrinter;
   }

   @Override
   public void moveNodeTo(final Object var1, final Point var2) {
      new DefaultOperationGraphController.LayoutChange() {
         @Override
         protected void doChange(OperationGraphLayout var1x) {
            var1x.move(var1, var2);
            DefaultOperationGraphController.this.graph.ensureNodeIsVisible(var1);
         }
      };
   }

   @Override
   public void toggleNodeExpansion(OperationWrapperNode var1) {
      this.toggleNodeExpansion(Collections.singletonList(var1));
   }

   @Override
   public void toggleNodeExpansion(List<OperationWrapperNode> var1) {
      NodeExpansionMemento var2 = this.graph.getNodeExpansionMemento();

      for (Object var4 : var1) {
         if (this.graph.getLayout().isExpanded(var4)) {
            this.graph.getLayout().collapse(var4);
         } else {
            this.graph.getLayout().expand(var4);
         }
      }

      NodeExpansionMemento var5 = this.graph.getNodeExpansionMemento();
      this.fireLayoutChanged(new NodeExpansionEdit(var2, var5));
   }

   @Override
   public void fireGraphDataEdit(UndoableEdit var1) {
      if (this.undoRedoMode == UndoRedoMode.ALL) {
         for (OperationGraphControllerListener var3 : this.controllerListeners) {
            var3.graphDataChanged(var1);
         }
      }
   }

   private void setOperationNodeController() {
      for (OperationWrapperNode var2 : this.graph.getModel().getOperationNodes()) {
         var2.setNodeController(this.opNodeController);
      }
   }

   private void registerKeyBindings() {
      this.graph.addKeyBinding("rename", new RenameNodeAction(this));
      this.graph.addKeyBinding("delete", new DeleteNodeAction(this));
      this.graph.addKeyBinding("toggle-expansion", new DefaultSingleNodeAction(this));
      this.graph.addKeyBinding("view-validation-messages", new ViewValidationMessagesAction(this.graph));
      this.graph.addKeyBinding("trigger-context-menu", new TriggerContextMenuAction(this));
      this.graph.addKeyBinding("print-graph", this.graphPrinter.getPrintAction());
      this.graph.addKeyBinding("show-optype-menu", new ShowActivityMenuAction(this));
      this.graph.addKeyBinding("show-success-menu", new ShowOnSuccessMenuAction(this));
      this.graph.addKeyBinding("show-failure-menu", new ShowOnFailureMenuAction(this));
      this.graph.addKeyBinding("jump-into-op", new JumpIntoOperationNodeAction(this.graph));
      this.graph.addKeyBinding("jump-outof-op", new JumpOutOfOperationNodeAction(this.graph));
      this.graph.addKeyBinding("reset-layout", new ResetLayoutAction(this));
      this.graph.addKeyBinding("clear-selection", new ClearSelectionAction(this.graph));
   }

   private void installAutoCompleters() {
      for (OperationWrapperNode var2 : this.graph.getModel().getOperationNodes()) {
         this.installAutoCompleter(var2);
      }
   }

   private void installAutoCompleter(OperationWrapperNode var1) {
      PipelineAutoCompleterTrigger var2 = new PipelineAutoCompleterTrigger(var1.getDataObject(), this.getEntityLookup());
      this.autoCompleters.put(var1, var2);
   }

   private void disposeAutoCompleters() {
      for (PipelineAutoCompleterTrigger var2 : this.autoCompleters.values()) {
         var2.dispose();
      }
   }

   private void fireLayoutChanged(UndoableEdit var1) {
      for (OperationGraphControllerListener var3 : this.controllerListeners) {
         var3.layoutWasChanged(var1);
      }
   }

   @Override
   public void saveProjectItem(IntegrationEntity var1) {
      IntegrationProjectLock.acquire();

      try {
         this.itemSaver.save(var1);
         this.itemSaver.saveProjectRelations();
      } catch (InterchangeSavingException var6) {
         ErrorLog.attention(this.getClass(), Strings.get("SaveNewItem.Error"), var6);
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private class CollapseAllOperationsAction extends AbstractAction {
      public CollapseAllOperationsAction() {
         super(Strings.get("CollapseAll.Name"));
         this.putValue("ShortDescription", Strings.get("CollapseAll.ToolTip"));
         this.setEnabled(this.isAtLeastOneNodeExpanded());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.isAtLeastOneNodeExpanded()) {
            NodeExpansionMemento var2 = DefaultOperationGraphController.this.graph.getNodeExpansionMemento();
            DefaultOperationGraphController.this.graph.getLayout().collapseAll();
            NodeExpansionMemento var3 = DefaultOperationGraphController.this.graph.getNodeExpansionMemento();
            DefaultOperationGraphController.this.fireLayoutChanged(new NodeExpansionEdit(var2, var3));
         }
      }

      private boolean isAtLeastOneNodeExpanded() {
         for (OperationWrapperNode var2 : DefaultOperationGraphController.this.graph.getModel().getOperationNodes()) {
            if (DefaultOperationGraphController.this.graph.getLayout().isExpanded(var2)) {
               return true;
            }
         }

         return false;
      }
   }

   private class InputHandler extends GraphController2D {
      private final ContextMenuRequestHandler contextMenuHandler = new ContextMenuRequestHandler(DefaultOperationGraphController.this);

      public InputHandler(OperationGraph2D var2) {
         super(var2);
         var2.setEdgeContextMenuProvider(this.contextMenuHandler);
         var2.setNavigationOrder(new GraphNavigationOrder(var2));
      }

      protected OperationGraph2D getGraph() {
         return (OperationGraph2D)super.getGraph();
      }

      @Override
      protected SelectionAction createToggleNodeExpansionAction() {
         return new ToggleOperationNodeExpansionAction(DefaultOperationGraphController.this);
      }

      @Override
      protected void handleNodeDoubleClick(Object var1) {
         DefaultSingleNodeAction var2 = new DefaultSingleNodeAction(DefaultOperationGraphController.this);
         var2.setSelection(SelectionFactory.newOrderedSelection(var1));
         ActionUtils.run(var2, this, "double-click");
      }

      @Override
      protected void handleCanvasDoubleClick() {
         List var1 = DefaultOperationGraphController.this.graph.getModel().getEdges();
         System.out.println("Edges: " + var1.size());

         for (Object __etlItem586 : var1) {
            GraphEdge var3 = (GraphEdge) __etlItem586;
            System.out.println(var3 + "  " + DefaultOperationGraphController.this.graph.getEdgeUi(var3));
         }

         System.out.println();
      }

      @Override
      protected void handlePopupMenuRequest(Object var1, Point var2) {
         if (var1 instanceof OperationWrapperNode) {
            this.contextMenuHandler.handle((OperationWrapperNode)var1, var2);
         } else if (var1 instanceof EmailWrapperNode) {
            this.contextMenuHandler.handle((EmailWrapperNode)var1, var2);
         } else if (var1 instanceof PipelineActivityNode) {
            this.contextMenuHandler.handle((PipelineActivityNode)var1, var2);
         } else {
            this.contextMenuHandler.handleCanvas(var2);
         }
      }

      @Override
      protected Collection<?> getNodesInRange(Object var1, Object var2) {
         if (var1 instanceof PipelineActivityNode && var2 instanceof PipelineActivityNode) {
            PipelineActivityNode var3 = (PipelineActivityNode)var1;
            PipelineActivityNode var4 = (PipelineActivityNode)var2;
            if (var3.getParent() == var4.getParent()) {
               List var5 = var3.getParent().getPipeline().getActivityRange(var3.getDataObject(), var4.getDataObject());
               ArrayList var6 = Lists.newArrayList();

               for (Object __etlItem615 : var5) {
                  PipelineActivity var8 = (PipelineActivity) __etlItem615;
                  var6.add(var3.getParent().getChildFor(var8));
               }

               return var6;
            }
         }

         return null;
      }
   }

   private abstract class LayoutChange {
      public LayoutChange() {
         GraphLayoutMemento var2 = DefaultOperationGraphController.this.graph.getLayout().getMemento();
         this.doChange(DefaultOperationGraphController.this.graph.getLayout());
         DefaultOperationGraphController.this.graph.recalculateSize();
         this.sendEdit(var2);
      }

      private void sendEdit(GraphLayoutMemento var1) {
         GraphLayoutEdit var2 = new GraphLayoutEdit(var1, DefaultOperationGraphController.this.graph.getLayout().getMemento());
         DefaultOperationGraphController.this.fireLayoutChanged(var2);
      }

      protected abstract void doChange(OperationGraphLayout var1);
   }

   private class ModelListener extends GraphModelAdapter {
      private ModelListener() {
      }

      @Override
      public void nodesInserted(NodesInsertedEvent var1) {
         for (Object var3 : var1.getInserted()) {
            if (var3 instanceof OperationWrapperNode) {
               OperationWrapperNode var4 = (OperationWrapperNode)var3;
               var4.setNodeController(DefaultOperationGraphController.this.opNodeController);
               DefaultOperationGraphController.this.installAutoCompleter(var4);
            }
         }
      }

      @Override
      public void nodesRemoved(NodesRemovedEvent var1) {
         for (Object var3 : var1.getRemoved()) {
            if (var3 instanceof OperationWrapperNode) {
               ((OperationWrapperNode)var3).setNodeController(null);
               PipelineAutoCompleterTrigger var4 = DefaultOperationGraphController.this.autoCompleters.remove(var3);
               if (var4 != null) {
                  var4.dispose();
               }
            }
         }
      }

      @Override
      public void modelChanged(GraphModelEvent var1) {
         for (OperationWrapperNode var3 : DefaultOperationGraphController.this.graph.getModel().getOperationNodes()) {
            var3.setNodeController(DefaultOperationGraphController.this.opNodeController);
         }
      }
   }

   private class NodeExpansionListenerImpl implements NodeExpansionListener {
      private NodeExpansionListenerImpl() {
      }

      @Override
      public void nodeCollapsed(NodeCollapsedEvent var1) {
         this.checkCollapseAllOperationsActionState(var1.getSource());
      }

      @Override
      public void nodeExpanded(NodeExpandedEvent var1) {
         this.checkCollapseAllOperationsActionState(var1.getSource());
      }

      private void checkCollapseAllOperationsActionState(GraphLayout2D var1) {
         for (OperationWrapperNode var3 : DefaultOperationGraphController.this.graph.getModel().getOperationNodes()) {
            if (var1.isExpanded(var3)) {
               DefaultOperationGraphController.this.collapseAllPipelinesAction.setEnabled(true);
               return;
            }
         }

         DefaultOperationGraphController.this.collapseAllPipelinesAction.setEnabled(false);
      }
   }
}
