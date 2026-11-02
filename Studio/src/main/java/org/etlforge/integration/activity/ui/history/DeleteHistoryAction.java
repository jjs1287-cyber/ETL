package org.etlforge.integration.activity.ui.history;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;

final class DeleteHistoryAction extends AbstractAction {
   private final SourceHistoryController controller;

   public DeleteHistoryAction(SourceHistoryController var1) {
      this.controller = var1;
      ActionDecorators.DELETE.decorate(this);
      this.putValue("ShortDescription", PackageResources.SourceDisplayer.Actions.DELETE_TOOLTIP);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeleteHistoryAction.this.controller.deleteSelectedFiles();
         }
      });
   }
}
