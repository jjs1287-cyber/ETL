package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.resources.CommonIcons;

final class SaveMappingsAction extends AbstractAction {
   private final ImportExportController controller;

   public SaveMappingsAction(ImportExportController var1) {
      super("&Save Mappings to File", CommonIcons.SAVE_16);
      this.controller = var1;
      this.putValue("ShortDescription", "Save the current mappings to a file so that they can be reused");
      this.setEnabled(false);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SaveMappingsAction.this.controller.exportMappings();
         }
      });
   }
}
