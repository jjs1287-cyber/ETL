package org.etlforge.integration.activity.ui.queue;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;

final class DeleteQueueAction extends AbstractAction {
   private final OperationQueueController controller;

   public DeleteQueueAction(OperationQueueController var1) {
      this.controller = var1;
      ActionDecorators.DELETE.decorate(this);
      this.putValue("ShortDescription", PackageResources.DELETE_TOOLTIP);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeleteQueueAction.this.controller.deleteSelectedEntries();
         }
      });
   }
}
