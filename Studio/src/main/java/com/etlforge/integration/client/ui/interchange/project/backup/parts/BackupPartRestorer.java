package com.etlforge.integration.client.ui.interchange.project.backup.parts;

import com.etlforge.integration.client.ui.interchange.project.backup.ProjectBackupBrowser;
import com.etlforge.integration.client.ui.interchange.project.backup.complete.CompleteBackupRestorer;
import com.etlforge.integration.client.ui.interchange.project.backup.complete.PreferencedBasedPrompt;
import com.etlforge.integration.client.ui.interchange.project.share.EntityImporter;
import com.etlforge.integration.client.ui.interchange.project.share.ShareProjectDataModel;
import com.etlforge.integration.client.ui.interchange.project.share.SourceProjectLoader;
import java.io.File;
import java.text.DateFormat;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.wait.WaitService;

public final class BackupPartRestorer implements Disposable {
   private final ApplicationWindow appWin;
   private final ProjectBackupBrowser browser;
   private final ShareProjectDataModel model;
   private RestoreBackupUi ui;
   private final EntityImporter worker;

   public BackupPartRestorer(ApplicationWindow var1, ProjectManager var2, ManagedProject var3, ProjectBackupBrowser var4) {
      this.appWin = var1;
      this.browser = var4;
      this.model = new ShareProjectDataModel(var2, var3);
      this.model.setSourceProjectTitle(this.getSourceTreeTitle(null));
      this.model.setTargetProjectTitle("Current Project");
      this.worker = new EntityImporter(this.model);
   }

   @Override
   public void dispose() {
      this.model.dispose();
   }

   public EntityImporter getWorker() {
      return this.worker;
   }

   public void loadBackup(AvailableBackup var1) {
      this.model.setSelectedBackup(var1);
      SourceProjectLoader var2 = new SourceProjectLoader(this.model);
      var2.setTitleWhenDone(this.getSourceTreeTitle(var1));
      WaitService var3 = this.ui != null ? this.ui.getWaitService() : WaitService.NONE;
      var2.load(var1.getLocation(), var3.startWait(125));
   }

   public void importItem(IntegrationEntity var1, IntegrationEntity var2) {
      this.worker.importItem(var1, var2);
   }

   public void restoreProject() {
      AvailableBackup var1 = this.model.getSelectedBackup();
      File var2 = this.model.getTargetProject().getLocation();
      final CompleteBackupRestorer var3 = new CompleteBackupRestorer(this.model.getProjectManager(), var1, var2);
      var3.setWaitService(this.appWin);
      var3.setPrompt(new PreferencedBasedPrompt(this.appWin.getFrame()));
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            try {
               var3.restore();
            } catch (Exception var2x) {
               BackupPartRestorer.this.logRestoreFailed(var2x);
            }
         }
      });
   }

   private void logRestoreFailed(Exception var1) {
      ErrorLog.attention(BackupPartRestorer.class, "Failed to restore the backup: " + var1.getMessage(), var1);
   }

   public UiProvider getUi() {
      if (this.ui == null) {
         this.ui = new RestoreBackupUi(this.appWin, this, this.model, this.browser);
         this.worker.setWaitService(this.ui.getWaitService());
      }

      return this.ui;
   }

   private String getSourceTreeTitle(AvailableBackup var1) {
      String var2 = "Selected Backup:";
      if (var1 != null) {
         DateFormat var3 = DateFormat.getDateTimeInstance(2, 3);
         var2 = var2 + " " + var3.format(var1.getTimestamp());
      }

      return var2;
   }
}
