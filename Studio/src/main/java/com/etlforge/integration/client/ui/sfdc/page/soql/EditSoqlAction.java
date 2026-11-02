package com.etlforge.integration.client.ui.sfdc.page.soql;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public final class EditSoqlAction extends AbstractAction {
   private final EditSoqlController controller;

   public EditSoqlAction(EditSoqlController var1) {
      super("Edit");
      this.controller = var1;
      this.putValue("ShortDescription", "Edit the query string");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EditSoqlAction.this.edit();
         }
      });
   }

   private void edit() {
      this.controller.edit();
   }
}
