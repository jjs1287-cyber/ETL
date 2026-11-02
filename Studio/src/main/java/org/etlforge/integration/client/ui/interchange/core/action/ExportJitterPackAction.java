package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.integration.client.ui.jitterpack.ExportJitterPackDialog;
import org.etlforge.integration.client.ui.jitterpack.ExportJitterPackUi;
import org.etlforge.ui.wait.WaitLock;

public final class ExportJitterPackAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.ExportJitterpak";
   private final ApplicationWindow appWin;
   private final Exporter exporter;
   private final SaveAllGate saveGate;

   public ExportJitterPackAction(ApplicationWindow var1, CurrentProjectService var2, Exporter var3, SaveAllGate var4) {
      super(var2, "action.ic.ExportJitterpak", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.exporter = var3;
      this.saveGate = var4;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExportJitterPackAction.this.saveGate.saveAndRun(ExportJitterPackAction.this.new Executor());
         }
      });
   }

   private final class Executor implements Runnable {
      private Executor() {
      }

      @Override
      public void run() {
         WaitLock var1 = ExportJitterPackAction.this.appWin.startWait();

         try {
            this.displayDialog();
         } finally {
            var1.release();
         }
      }

      private void displayDialog() {
         ExportJitterPackUi var1 = new ExportJitterPackUi(ExportJitterPackAction.this.getProject());
         ExportJitterPackDialog.show(ExportJitterPackAction.this.appWin, var1, ExportJitterPackAction.this.exporter);
      }
   }
}
