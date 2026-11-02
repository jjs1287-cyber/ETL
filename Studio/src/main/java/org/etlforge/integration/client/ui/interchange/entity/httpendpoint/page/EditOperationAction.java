package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperation;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

final class EditOperationAction extends AbstractSingleObjectSelectionAction<TriggeredOperation> {
   private EntityExplorerSupport explorerSupport;

   public EditOperationAction() {
      super(TriggeredOperation.class, "Edit Operation...");
   }

   public void setExplorerSupport(EntityExplorerSupport var1) {
      this.explorerSupport = var1;
   }

   protected boolean isEnabledFor(TriggeredOperation var1) {
      return this.explorerSupport != null;
   }

   protected void run(TriggeredOperation var1, ActionEvent var2) {
      if (this.explorerSupport != null) {
         IntegrationEntityLookup var3 = this.explorerSupport.getProject().getItemLookup();
         Operation var4 = var3.getEntity(var1.getOperationId(), Operation.class);
         if (var4 != null) {
            this.explorerSupport.openPageFor(var4);
         }
      }
   }
}
