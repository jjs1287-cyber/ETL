package org.etlforge.integration.client.ui.interchange.component.tree;

import java.util.Collections;
import java.util.List;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.integration.client.ui.entity.TransferableIntegrationEntities;
import org.etlforge.integration.client.ui.entity.move.EntityMover;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;

final class EntityRootDropHandler extends TransferHandler {
   private final CompoundTreeDropStrategy strategies;
   private RootFolder target;

   public EntityRootDropHandler(EntityMover var1) {
      DragAndDropEntityMover var2 = new DragAndDropEntityMover(var1);
      this.strategies = new CompoundTreeDropStrategy(var2);
   }

   public void addDropStrategy(TreeDropStrategy var1) {
      this.strategies.add(var1);
   }

   public void setTarget(RootFolder var1) {
      this.target = var1;
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      if (this.target == null) {
         return false;
      }

      if (!var1.isDataFlavorSupported(TransferableIntegrationEntities.ENTITIES_FLAVOR)) {
         return false;
      }

      List var2 = this.getDraggedItems(var1);
      return this.strategies.canDrop(this.target, var2);
   }

   @Override
   public boolean importData(TransferSupport var1) {
      if (this.target != null) {
         List var2 = this.getDraggedItems(var1);
         return this.strategies.handleDrop(this.target, var2);
      } else {
         return false;
      }
   }

   private List<IntegrationEntity> getDraggedItems(TransferSupport var1) {
      try {
         List var2 = (List)var1.getTransferable().getTransferData(TransferableIntegrationEntities.ENTITIES_FLAVOR);
         return KongaListUtils.transform(var2, KongaFunctions.cast(IntegrationEntity.class));
      } catch (Exception var3) {
         var3.printStackTrace();
         return Collections.emptyList();
      }
   }
}
