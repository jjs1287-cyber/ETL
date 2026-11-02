package com.etlforge.integration.client.ui.interchange.core.action;

import com.etlforge.integration.client.ui.interchange.project.backup.ProjectBackupListBrowser;
import com.etlforge.integration.client.ui.interchange.project.backup.parts.BackupPartRestorer;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.client.project.backup.ProjectBackupService;
import org.etlforge.integration.client.ui.interchange.core.action.AbstractProjectBasedAction;
import org.etlforge.integration.client.ui.interchange.core.action.SaveAllGate;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.dialog.KongaDialog;

public final class RestoreBackupAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.RestoreBackup";
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private final SaveAllGate saveGate;
   private KongaDialog dialog;

   public RestoreBackupAction(ApplicationWindow var1, CurrentProjectService var2, ProjectManager var3, SaveAllGate var4) {
      super(var2, "action.ic.RestoreBackup", EnterpriseActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.projectManager = var3;
      this.saveGate = var4;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RestoreBackupAction.this.saveGate.saveAndRun(RestoreBackupAction.this.new Launcher());
         }
      });
   }

   @Override
   protected void onProjectClosed(IntegrationProject var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (RestoreBackupAction.this.dialog != null && RestoreBackupAction.this.dialog.isVisible()) {
               RestoreBackupAction.this.dialog.dispose();
               RestoreBackupAction.this.dialog = null;
            }
         }
      });
   }

   private final class Launcher implements Runnable {
      private Launcher() {
      }

      @Override
      public void run() {
         RestoreBackupAction.UiCreator var1 = RestoreBackupAction.this.new UiCreator();
         var1.submit(RestoreBackupAction.this.appWin, 0);
      }
   }

   private final class UiCreator extends UiJob {
      private AvailableBackup[] backups;
      private BackupPartRestorer restorer;

      public UiCreator() {
         super("Open Project Backup Console");
      }

      @Override
      protected void runImpl() throws InterruptedException {
         this.setMessage("Collecting available backups");
         ProjectBackupService var1 = RestoreBackupAction.this.projectManager.getBackupService();
         this.backups = var1.getAvailableBackups(RestoreBackupAction.this.getProject().getID());
         this.setMessage("Publishing the UI");
      }

      private BackupPartRestorer createRestorer() {
         ProjectBackupListBrowser var1 = new ProjectBackupListBrowser(this.backups);
         return new BackupPartRestorer(
            RestoreBackupAction.this.appWin, RestoreBackupAction.this.projectManager, RestoreBackupAction.this.getManagedProject(), var1
         );
      }

      @Override
      protected void completed() {
         this.restorer = this.createRestorer();
         this.showDialog();
      }

      private void showDialog() {
         RestoreBackupAction.this.dialog = new WizardStyleDialog(
            RestoreBackupAction.this.appWin.getFrame(), "Restore From Backup", this.restorer.getUi(), KongaDialog.CLOSE_ONLY, true
         );
         RestoreBackupAction.this.dialog.manageLocation(BackupPartRestorer.class);
         RestoreBackupAction.this.dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent var1) {
               UiCreator.this.restorer.dispose();
               RestoreBackupAction.this.dialog = null;
               UiCreator.this.restorer = null;
            }
         });
         RestoreBackupAction.this.dialog.showLater();
      }
   }
}
