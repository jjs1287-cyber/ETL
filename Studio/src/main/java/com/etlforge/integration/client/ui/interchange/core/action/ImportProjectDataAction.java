package com.etlforge.integration.client.ui.interchange.core.action;

import com.etlforge.integration.client.ui.interchange.project.share.ProjectItemImporter;
import com.etlforge.integration.client.ui.interchange.project.share.ProjectItemImporterFactory;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.core.action.AbstractProjectBasedAction;
import org.etlforge.integration.client.ui.interchange.core.action.SaveAllGate;
import org.etlforge.ui.dialog.KongaDialog;

public final class ImportProjectDataAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.ImportProjectData";
   private final ApplicationWindow appWin;
   private final ProjectItemImporterFactory importFactory;
   private final SaveAllGate saveGate;

   public ImportProjectDataAction(ApplicationWindow var1, CurrentProjectService var2, ProjectItemImporterFactory var3, SaveAllGate var4) {
      super(var2, "action.ic.ImportProjectData", EnterpriseActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.importFactory = var3;
      this.saveGate = var4;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ImportProjectDataAction.this.saveGate.saveAndRun(ImportProjectDataAction.this.new Launcher());
         }
      });
   }

   private class Launcher implements Runnable {
      private Launcher() {
      }

      @Override
      public void run() {
         ProjectItemImporter var1 = ImportProjectDataAction.this.importFactory.newImporter(ImportProjectDataAction.this.getManagedProject());
         KongaDialog var2 = this.createDialog(var1);
         var2.setVisible(true);
      }

      private KongaDialog createDialog(final ProjectItemImporter var1) {
         WizardStyleDialog var2 = new WizardStyleDialog(
            ImportProjectDataAction.this.appWin.getFrame(), "Import Project Items", var1.getUi(), KongaDialog.CLOSE_ONLY, true
         );
         var2.manageLocation(ImportProjectDataAction.class);
         var2.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent var1x) {
               var1.dispose();
            }
         });
         return var2;
      }
   }
}
