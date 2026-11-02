package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import com.google.common.collect.Lists;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;

final class ActivityConnectionDropBehavior extends DropBehavior {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;
   private final ActivityConnection connection;
   private Collection<Object> newSelection;

   public ActivityConnectionDropBehavior(OperationGraphController var1, OperationWrapperNode var2, ActivityConnection var3) {
      this.controller = var1;
      this.opNode = var2;
      this.connection = var3;
   }

   @Override
   public boolean isDropAllowed(DropTargetDragEvent var1) {
      IntegrationEntity var2 = this.getDraggedEntity(var1);
      return this.getAllowedType(var2) != null;
   }

   @Override
   public boolean handleDrop(DropTargetDropEvent var1) {
      IntegrationEntity var2 = this.getDroppedEntity(var1);
      if (var2 == null) {
         return false;
      }

      PipelineActivityType var3 = this.getAllowedType(var2);
      PipelineActivity var4 = var3.createEmptyActivity();
      if (!(var4 instanceof EntityActivity)) {
         return false;
      }

      ((EntityActivity)var4).setContent(var2);
      boolean var5 = this.controller.insertNewActivity(this.opNode.getPipeline(), this.connection, var4);
      this.updateSelection(var2, var5);
      return var5;
   }

   private void updateSelection(IntegrationEntity var1, boolean var2) {
      if (var2) {
         this.newSelection = Lists.newArrayList();
         PipelineActivityNode var3 = this.findInsertedNode(var1);
         if (var3 != null) {
            this.newSelection.add(var3);
         }
      } else {
         this.newSelection = null;
      }
   }

   private PipelineActivityNode findInsertedNode(IntegrationEntity var1) {
      for (PipelineActivityNode var3 : this.opNode.getChildren()) {
         if (var3.getDataObject().getContent() == var1) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public Collection<?> getNewSelection() {
      return this.newSelection;
   }

   private PipelineActivityType getAllowedType(IntegrationEntity var1) {
      if (var1 == null) {
         return null;
      }

      ActivityInsertionPolicy var2 = this.opNode.getPipeline().getInsertionPolicy();

      for (PipelineActivityType var4 : var2.getAllowedActivities(this.connection)) {
         if (var4.accepts(var1)) {
            return var4;
         }
      }

      return null;
   }
}
