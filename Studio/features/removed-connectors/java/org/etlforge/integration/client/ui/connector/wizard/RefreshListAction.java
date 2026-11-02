package org.etlforge.integration.client.ui.connector.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;

public final class RefreshListAction extends AbstractAction {
   private final ObjectSelectionController controller;

   public RefreshListAction(ObjectSelectionController var1) {
      this.controller = var1;
      ActionDecorators.REFRESH.decorate(this);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RefreshListAction.this.controller.refreshObjectList();
         }
      });
   }
}
