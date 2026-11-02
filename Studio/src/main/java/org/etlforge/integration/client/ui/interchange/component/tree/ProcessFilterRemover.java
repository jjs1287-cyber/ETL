package org.etlforge.integration.client.ui.interchange.component.tree;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.etlforge.integration.client.ui.interchange.ProcessFilterInstaller;

final class ProcessFilterRemover implements ActionListener {
   private final ProcessFilterInstaller installer;

   public ProcessFilterRemover(ProcessFilterInstaller var1) {
      this.installer = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProcessFilterRemover.this.installer.removeFilter();
         }
      });
   }
}
