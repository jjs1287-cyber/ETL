package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Point;
import java.util.List;
import javax.swing.Action;
import javax.swing.undo.UndoableEdit;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphManager;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
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
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.Disposable;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.print.GenericPrinter;

public interface OperationGraphController extends Disposable {
   OperationGraphController READ_ONLY = new OperationGraphController() {
      @Override
      public ApplicationWindow getWindow() {
         return null;
      }

      @Override
      public Operation getMainOperation() {
         return null;
      }

      @Override
      public void deployMainOperation() {
      }

      @Override
      public void addControllerListener(OperationGraphControllerListener var1) {
      }

      @Override
      public void clearNodeContent(PipelineActivityNode var1) {
      }

      @Override
      public boolean deleteNode(AbstractGraphNode var1) {
         return false;
      }

      @Override
      public void deleteRoute(Route var1) {
      }

      @Override
      public void dispose() {
      }

      @Override
      public void fireGraphDataEdit(UndoableEdit var1) {
      }

      @Override
      public Action getCollapseAllPipelinesAction() {
         return null;
      }

      @Override
      public UiEntityFactory getEntityFactory() {
         return null;
      }

      @Override
      public IntegrationEntityLookup getEntityLookup() {
         return null;
      }

      @Override
      public EntityExplorerSupport getExplorerSupport() {
         return null;
      }

      @Override
      public OperationGraph2D getGraph() {
         return null;
      }

      @Override
      public OperationGraphManager getGraphManager() {
         return null;
      }

      @Override
      public GenericPrinter getGraphPrinter() {
         return null;
      }

      @Override
      public Editor openEditor(IntegrationEntity var1) {
         return null;
      }

      @Override
      public IntegrationProject getProject() {
         return null;
      }

      @Override
      public boolean insertNewActivity(OperationPipeline var1, ActivityConnection var2, PipelineActivity var3) {
         return false;
      }

      @Override
      public boolean isChangeAllowed(OperationWrapper var1) {
         return false;
      }

      @Override
      public boolean isChangeAllowed(OperationWrapperNode var1) {
         return false;
      }

      @Override
      public boolean isChangeAllowed(PipelineActivityNode var1) {
         return false;
      }

      @Override
      public boolean isDeletable(AbstractGraphNode var1) {
         return false;
      }

      @Override
      public void moveNodeTo(Object var1, Point var2) {
      }

      @Override
      public void removeControllerListener(OperationGraphControllerListener var1) {
      }

      @Override
      public void renameNode(AbstractGraphNode var1) {
      }

      @Override
      public void resetLayout() {
      }

      @Override
      public void resetOperationNodeLayout(OperationWrapperNode var1) {
      }

      @Override
      public void selectActivity(PipelineActivityNode var1) {
      }

      @Override
      public <E extends IntegrationEntity> boolean setActivityNodeContent(OperationWrapper var1, MutablePipelineActivity<E> var2, E var3) {
         return false;
      }

      @Override
      public void setGraph(OperationGraph2D var1) {
      }

      @Override
      public boolean setNextEmail(OperationWrapperNode var1, EmailMessage var2, RouteType var3) {
         return false;
      }

      @Override
      public boolean setNextOperation(OperationWrapperNode var1, Operation var2, RouteType var3) {
         return false;
      }

      @Override
      public void setOperationName(OperationWrapperNode var1, String var2) {
      }

      @Override
      public void openOptionsUi(OperationWrapperNode var1) {
      }

      @Override
      public void setOperationOptions(OperationWrapperNode var1, OperationOptions var2) {
      }

      @Override
      public void setOperationType(OperationWrapperNode var1, OperationType var2) {
      }

      @Override
      public boolean setSchedule(OperationWrapperNode var1, Schedule var2) {
         return false;
      }

      @Override
      public void setSchedulePolicy(OperationWrapperNode var1, SchedulePolicy var2) {
      }

      @Override
      public void setSiblings(OperationWrapper var1, List<Operation> var2) {
      }

      @Override
      public void toggleNodeExpansion(List<OperationWrapperNode> var1) {
      }

      @Override
      public void toggleNodeExpansion(OperationWrapperNode var1) {
      }

      @Override
      public void saveProjectItem(IntegrationEntity var1) {
      }

      @Override
      public Action getRunOperationAction() {
         return null;
      }

      @Override
      public Action getDeploySelectedItemsAction() {
         return null;
      }

      @Override
      public Action getSetPermissionsAction() {
         return null;
      }

      @Override
      public Action getAssignPluginAction() {
         return null;
      }

      @Override
      public Action getTestConnectionAction() {
         return null;
      }

      @Override
      public Action getViewDependenciesAction() {
         return null;
      }

      @Override
      public OperationRunnerFactory getRunnerFactory() {
         return null;
      }
   };

   void setGraph(OperationGraph2D var1);

   Operation getMainOperation();

   ApplicationWindow getWindow();

   OperationGraph2D getGraph();

   OperationGraphManager getGraphManager();

   IntegrationProject getProject();

   UiEntityFactory getEntityFactory();

   EntityExplorerSupport getExplorerSupport();

   IntegrationEntityLookup getEntityLookup();

   Editor openEditor(IntegrationEntity var1);

   void deployMainOperation();

   Action getCollapseAllPipelinesAction();

   boolean isChangeAllowed(OperationWrapperNode var1);

   boolean isChangeAllowed(OperationWrapper var1);

   boolean isChangeAllowed(PipelineActivityNode var1);

   void setOperationName(OperationWrapperNode var1, String var2);

   void setOperationType(OperationWrapperNode var1, OperationType var2);

   void openOptionsUi(OperationWrapperNode var1);

   void setOperationOptions(OperationWrapperNode var1, OperationOptions var2);

   boolean setSchedule(OperationWrapperNode var1, Schedule var2);

   void setSchedulePolicy(OperationWrapperNode var1, SchedulePolicy var2);

   boolean setNextOperation(OperationWrapperNode var1, Operation var2, RouteType var3);

   void saveProjectItem(IntegrationEntity var1);

   boolean setNextEmail(OperationWrapperNode var1, EmailMessage var2, RouteType var3);

   void setSiblings(OperationWrapper var1, List<Operation> var2);

   void deleteRoute(Route var1);

   void renameNode(AbstractGraphNode var1);

   boolean isDeletable(AbstractGraphNode var1);

   boolean deleteNode(AbstractGraphNode var1);

   <E extends IntegrationEntity> boolean setActivityNodeContent(OperationWrapper var1, MutablePipelineActivity<E> var2, E var3);

   void clearNodeContent(PipelineActivityNode var1);

   void addControllerListener(OperationGraphControllerListener var1);

   void removeControllerListener(OperationGraphControllerListener var1);

   void resetLayout();

   void resetOperationNodeLayout(OperationWrapperNode var1);

   boolean insertNewActivity(OperationPipeline var1, ActivityConnection var2, PipelineActivity var3);

   void selectActivity(PipelineActivityNode var1);

   GenericPrinter getGraphPrinter();

   void moveNodeTo(Object var1, Point var2);

   void fireGraphDataEdit(UndoableEdit var1);

   void toggleNodeExpansion(OperationWrapperNode var1);

   void toggleNodeExpansion(List<OperationWrapperNode> var1);

   Action getRunOperationAction();

   Action getDeploySelectedItemsAction();

   Action getSetPermissionsAction();

   Action getAssignPluginAction();

   Action getTestConnectionAction();

   Action getViewDependenciesAction();

   OperationRunnerFactory getRunnerFactory();
}
