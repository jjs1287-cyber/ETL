package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import com.google.common.collect.Lists;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.PipelineActivityContentSetter;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;

public class PipelineActivityDropBehavior extends DropBehavior {
   private final OperationGraphController controller;
   private final PipelineActivityNode node;
   private Collection<?> newSelection;

   public PipelineActivityDropBehavior(OperationGraphController var1, PipelineActivityNode var2) {
      this.controller = var1;
      this.node = var2;
   }

   @Override
   public boolean isDropAllowed(DropTargetDragEvent var1) {
      IntegrationEntity var2 = this.getDraggedEntity(var1);
      return var2 != null && this.node.getDataObject().getType().accepts(var2);
   }

   @Override
   public boolean handleDrop(DropTargetDropEvent var1) {
      IntegrationEntity var2 = this.getDroppedEntity(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = new PipelineActivityContentSetter<IntegrationEntity>(this.controller)
         .setContent(this.node.getParent().getDataObject(), (EntityActivity)this.node.getDataObject(), var2);
      this.newSelection = var3 ? Lists.newArrayList(this.node) : null;
      return var3;
   }

   @Override
   public Collection<?> getNewSelection() {
      return this.newSelection;
   }

   protected final PipelineActivityNode getNode() {
      return this.node;
   }

   protected final OperationGraphController getController() {
      return this.controller;
   }
}
