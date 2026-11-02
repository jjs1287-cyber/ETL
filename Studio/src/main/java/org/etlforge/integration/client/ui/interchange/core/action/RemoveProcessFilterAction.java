package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.ProcessFilterInstaller;

public final class RemoveProcessFilterAction extends AbstractAction {
   private final ProcessFilterInstaller installer;

   public RemoveProcessFilterAction(ProcessFilterInstaller var1) {
      this.installer = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RemoveProcessFilterAction.this.installer.removeFilter();
         }
      });
   }
}
