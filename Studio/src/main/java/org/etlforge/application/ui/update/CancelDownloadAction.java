package org.etlforge.application.ui.update;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.job.ProcessConsoleIcons;
import org.etlforge.system.thread.Cancelable;

final class CancelDownloadAction extends AbstractAction {
   private final Cancelable job;

   public CancelDownloadAction(Cancelable var1) {
      super("Cancel download", ProcessConsoleIcons.STOP);
      this.job = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CancelDownloadAction.this.job.cancel();
         }
      });
   }
}
