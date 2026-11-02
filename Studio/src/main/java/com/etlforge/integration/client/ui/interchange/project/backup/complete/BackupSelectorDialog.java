package com.etlforge.integration.client.ui.interchange.project.backup.complete;

import com.google.common.base.Throwables;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.LayeredKongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;

public final class BackupSelectorDialog implements BackupSelector {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private volatile ProjectLocation selectedBackup;
   private KongaDialog dialog;

   public BackupSelectorDialog(ApplicationWindow var1, ProjectManager var2) {
      this.appWin = var1;
      this.projectManager = var2;
   }

   @Override
   public ProjectLocation selectBackup(ProjectLocation var1) {
      this.selectedBackup = null;
      this.openDialog(var1);
      return this.selectedBackup;
   }

   private void openDialog(ProjectLocation var1) {
      WaitLock var2 = this.appWin.startWait();
      this.constructAndShowUi(var1, var2);
   }

   private void constructAndShowUi(ProjectLocation var1, WaitLock var2) {
      try {
         BackupSelectorUi var3 = new BackupSelectorUi(this.projectManager);
         var3.setProjectHandler(new Receiver<ProjectLocation>() {
            public void handle(ProjectLocation var1) {
               this.disposeDialog();
               BackupSelectorDialog.this.selectedBackup = var1;
            }

            private void disposeDialog() {
               EventQueue.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     BackupSelectorDialog.this.dialog.dispose();
                  }
               });
            }
         });
         this.dialog = this.createDialog(var3, var1, var2);
         this.dialog.manageLocation(BackupSelectorUi.class);
         this.dialog.setVisible(true);
      } catch (Exception var4) {
         var2.release();
         throw Throwables.propagate(var4);
      }
   }

   private LayeredKongaDialog createDialog(final BackupSelectorUi var1, final ProjectLocation var2, final WaitLock var3) {
      LayeredKongaDialog var4 = new LayeredKongaDialog(this.appWin.getFrame(), "Restore Backup", true) {
         @Override
         protected void onDialogOpened() {
            var3.release();
            var1.retrieveAvailableBackups(var2);
         }
      };
      var1.setCancelAction(this.getCancelAction(var4));
      this.layoutDialog(var1, var4);
      var1.setWaitService(var4);
      var4.pack();
      UiUtils.makeDefaultButton(var4.getRootPane(), var1.getRestoreAction());
      return var4;
   }

   private void layoutDialog(BackupSelectorUi var1, LayeredKongaDialog var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      var3.center(var1);
      var3.withBorder(Empty.border(10));
      var2.getDefaultLayout().center(var3);
   }

   private Action getCancelAction(final KongaDialog var1) {
      return new AbstractAction(ApplicationResources.STRINGS.getString("Cancel.Action")) {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.dispose();
         }
      };
   }
}
