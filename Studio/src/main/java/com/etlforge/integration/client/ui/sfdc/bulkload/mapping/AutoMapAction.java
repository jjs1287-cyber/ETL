package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;

final class AutoMapAction extends AbstractAction {
   private final HeaderMappingsModel model;

   public AutoMapAction(HeaderMappingsModel var1) {
      super("&Auto Map", ClientIcons.AUTO_MAPPER_16);
      this.model = var1;
      this.putValue("ShortDescription", "Automatically map fields with similar names");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AutoMapAction.this.model.autoMap();
         }
      });
   }
}
