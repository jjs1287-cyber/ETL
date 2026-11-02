package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

final class ImportMappingsAction extends AbstractAction {
   private final ImportExportController controller;

   public ImportMappingsAction(ImportExportController var1) {
      super("&Import Mappings from File");
      this.controller = var1;
      this.putValue("ShortDescription", "Import previously saved mappings");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ImportMappingsAction.this.controller.importMappings();
         }
      });
   }
}
