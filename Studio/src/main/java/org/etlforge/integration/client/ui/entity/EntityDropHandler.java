package org.etlforge.integration.client.ui.entity;

import java.util.List;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.IntegrationEntity;

public abstract class EntityDropHandler extends TransferHandler {
   protected boolean accepts(IntegrationEntity var1, TransferSupport var2) {
      return true;
   }

   protected abstract boolean handleDrop(IntegrationEntity var1, TransferSupport var2);

   @Override
   public final boolean canImport(TransferSupport var1) {
      if (this.acceptsDropAction(var1.getDropAction())) {
         IntegrationEntity var2 = this.getDraggedEntity(var1);
         if (var2 != null) {
            return this.accepts(var2, var1);
         }
      }

      return false;
   }

   protected boolean acceptsDropAction(int var1) {
      return true;
   }

   @Override
   public final boolean importData(TransferSupport var1) {
      IntegrationEntity var2 = this.getDraggedEntity(var1);
      return var2 != null && this.handleDrop(var2, var1);
   }

   private IntegrationEntity getDraggedEntity(TransferSupport var1) {
      if (!var1.isDataFlavorSupported(TransferableIntegrationEntities.ENTITIES_FLAVOR)) {
         return null;
      }

      try {
         List var2 = (List)var1.getTransferable().getTransferData(TransferableIntegrationEntities.ENTITIES_FLAVOR);
         if (var2 != null && var2.size() == 1) {
            return (IntegrationEntity)var2.get(0);
         }
      } catch (Exception var3) {
         ErrorLog.log(this.getClass(), "An unexpected error occurred", var3);
      }

      return null;
   }
}
