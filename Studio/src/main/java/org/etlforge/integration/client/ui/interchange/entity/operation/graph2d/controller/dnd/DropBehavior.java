package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.entity.TransferableIntegrationEntities;
import org.etlforge.integration.data.entity.IntegrationEntity;

public abstract class DropBehavior {
   public abstract boolean isDropAllowed(DropTargetDragEvent var1);

   public abstract boolean handleDrop(DropTargetDropEvent var1);

   public abstract Collection<?> getNewSelection();

   protected final IntegrationEntity getDraggedEntity(DropTargetDragEvent var1) {
      return this.getEntity(var1.getCurrentDataFlavors(), var1.getTransferable());
   }

   protected final IntegrationEntity getDroppedEntity(DropTargetDropEvent var1) {
      return this.getEntity(var1.getCurrentDataFlavors(), var1.getTransferable());
   }

   private IntegrationEntity getEntity(DataFlavor[] var1, Transferable var2) {
      boolean var3 = false;

      for (DataFlavor var7 : var1) {
         if (var7 == TransferableIntegrationEntities.ENTITIES_FLAVOR) {
            var3 = true;
            break;
         }
      }

      if (!var3) {
         return null;
      }

      try {
         List var10 = (List)var2.getTransferData(TransferableIntegrationEntities.ENTITIES_FLAVOR);
         if (var10 != null && var10.size() == 1) {
            return (IntegrationEntity)var10.get(0);
         }
      } catch (UnsupportedFlavorException var8) {
         ErrorLog.log(DropBehavior.class, "An unexpected error occurred", var8);
      } catch (IOException var9) {
         ErrorLog.attention(DropBehavior.class, "An error occurred while completing the drop process", var9);
      }

      return null;
   }
}
