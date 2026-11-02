package org.etlforge.integration.client.ui.entity.selector.combo;

import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.integration.client.ui.entity.EntityDropHandler;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.event.ApplicationMessageEvent;

final class DropHandler extends EntityDropHandler {
   private final EntityComboBoxModel model;

   public DropHandler(EntityComboBoxModel var1) {
      this.model = var1;
   }

   @Override
   protected boolean accepts(IntegrationEntity var1, TransferSupport var2) {
      if (this.model.accept(var1)) {
         return true;
      }

      this.postIllegalDrop();
      return false;
   }

   @Override
   protected boolean handleDrop(IntegrationEntity var1, TransferSupport var2) {
      this.model.setSelectedItem(var1);
      return true;
   }

   private void postIllegalDrop() {
      ApplicationMessageEvent.postErrorMessage(this, Strings.get("EntityCombo.Message.InvalidDrop"));
   }
}
