package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class ActivityContextMenuFactory {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;
   private final PipelineActivityNode activityNode;
   private final KongaPopupMenu popup;
   private final ContentProviderGate gate;

   public ActivityContextMenuFactory(OperationGraphController var1, PipelineActivityNode var2) {
      this.controller = var1;
      this.activityNode = var2;
      this.opNode = var2.getParent();
      this.gate = new ContentProviderGate(var1, this.opNode);
      this.popup = new KongaPopupMenu();
      this.popup
         .addActions(this.getEditActions(var2))
         .addActions(new RenameNodeAction(var1, var2), var1.getViewDependenciesAction(), new LocateInTreeAction(var2));
      this.addViewValidationMessagesAction(var2);
      this.popup.addActions(var1.getDeploySelectedItemsAction());
      if (var1.getSetPermissionsAction() != null) {
         this.popup.addActions(new ActivityContextMenuFactory.SetPermissionsAction(var1, var2));
         this.popup.addSeparator();
      }

      this.handle(var2.getDataObject());
      this.checkPreConnectionPoint(var2);
      this.checkPostConnectionPoint(var2);
      this.popup.addActions(new ClearActivityContentAction(var1, var2), null, new DeleteNodeAction(var1, var2));
   }

   private List<Action> getEditActions(PipelineActivityNode var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add(new EditActivityContentAction(var1));
      return var2;
   }

   public JPopupMenu getPopupMenu() {
      return this.popup;
   }

   private void handle(PipelineActivity var1) {
      OperationGraphConfig var2 = this.controller.getGraph().getGraphConfig();
      PipelineActivityType var3 = var1.getType();
      ActivityTypeConfig var4 = var2.getTypeConfig(var3);
      List var5 = var4.getContextMenuActions(this.activityNode, this.controller, this.gate);
      this.popup.addActions(var5);
   }

   private void addViewValidationMessagesAction(PipelineActivityNode var1) {
      this.popup.add(new ViewValidationMessagesAction(this.controller.getGraph(), var1));
   }

   private void checkPreConnectionPoint(PipelineActivityNode var1) {
      if (this.hasPreConnectionPoint(var1)) {
         ActivityConnection var2 = new ActivityConnection(null, var1.getDataObject());
         this.insertConnectionPointMenu(var1, var2, "Insert Before This");
      }
   }

   private void checkPostConnectionPoint(PipelineActivityNode var1) {
      if (this.hasPostConnectionPoint(var1)) {
         ActivityConnection var2 = new ActivityConnection(var1.getDataObject(), null);
         this.insertConnectionPointMenu(var1, var2, "Insert After This");
      }
   }

   private void insertConnectionPointMenu(PipelineActivityNode var1, ActivityConnection var2, String var3) {
      InsertionPointContextMenuFactory var4 = new InsertionPointContextMenuFactory(this.controller, var3, GraphResources.ICONS.getIcon("InsertionPoint.Yellow"));
      OperationPipeline var5 = var1.getParent().getPipeline();
      this.popup.add(var4.createMenu(var5, var2));
   }

   private boolean hasPreConnectionPoint(PipelineActivityNode var1) {
      OperationPipeline var2 = var1.getParent().getPipeline();
      return var2.getType().hasPreConnectionPoint() && var2.getFirstActivity() == var1.getDataObject();
   }

   private boolean hasPostConnectionPoint(PipelineActivityNode var1) {
      OperationPipeline var2 = var1.getParent().getPipeline();
      return var2.getLastActivity() == var1.getDataObject() && var2.hasPostConnectionPoint();
   }

   private static class SetPermissionsAction extends AbstractAction {
      private final Action impl;

      public SetPermissionsAction(OperationGraphController var1, PipelineActivityNode var2) {
         this.impl = var1.getSetPermissionsAction();
         this.setEnabled(isApplicableFor(var2));
      }

      @Override
      public Object getValue(String var1) {
         return this.impl.getValue(var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.impl.actionPerformed(var1);
      }

      private static boolean isApplicableFor(PipelineActivityNode var0) {
         Object var1 = var0.getDataObject().getContent();
         return var1 instanceof IntegrationEntity && ((IntegrationEntity)var1).isEditable();
      }
   }
}
