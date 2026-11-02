package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Objects;
import java.util.List;
import org.etlforge.integration.client.ui.entity.move.EntityMover;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class DragAndDropEntityMover implements TreeDropStrategy {
   private final EntityMover mover;
   private List<? extends IntegrationEntity> dragged;

   public DragAndDropEntityMover(EntityMover var1) {
      this.mover = var1;
   }

   @Override
   public boolean canDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2) {
      this.mover.setItemsToMove(var2);
      boolean var3 = this.mover.isAllowedTarget(var1);
      if (var3) {
         this.dragged = var2;
      } else {
         this.dragged = null;
      }

      return var3;
   }

   @Override
   public boolean handleDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2) {
      try {
         if (Objects.equal(var2, this.dragged)) {
            this.mover.executeMove(var1);
            return true;
         } else {
            return false;
         }
      } finally {
         this.dragged = null;
      }
   }
}
