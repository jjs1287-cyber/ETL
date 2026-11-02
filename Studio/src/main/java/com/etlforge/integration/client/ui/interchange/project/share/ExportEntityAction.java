package com.etlforge.integration.client.ui.interchange.project.share;

import com.etlforge.integration.client.ui.interchange.project.backup.complete.CompleteBackupRestorer;
import com.etlforge.integration.client.ui.interchange.project.backup.complete.PreferencedBasedPrompt;
import java.awt.event.ActionEvent;
import java.io.File;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class ExportEntityAction extends AbstractSingleObjectSelectionAction<IntegrationEntity> {
   private final ApplicationWindow appWin;
   private final ShareProjectDataModel model;
   private final EntityImporter worker;
   private boolean exportProjectSupported = true;

   public ExportEntityAction(ApplicationWindow var1, ShareProjectDataModel var2, EntityImporter var3) {
      super(IntegrationEntity.class, "Import Reference into Target Project", ApplicationResources.ICONS.getIcon("ArrowRight.16"));
      this.model = var2;
      this.worker = var3;
      this.appWin = var1;
      this.putValue("ShortDescription", "Import the selected item into the target project");
   }

   public void setExportProjectSupported(boolean var1) {
      this.exportProjectSupported = var1;
   }

   protected boolean isEnabledFor(IntegrationEntity var1) {
      return this.isExportable(var1);
   }

   private boolean isExportable(IntegrationEntity var1) {
      return var1 instanceof IntegrationProject ? this.exportProjectSupported : !(var1 instanceof Folder) && !(var1 instanceof BuiltInIntegrationEntity);
   }

   protected void run(IntegrationEntity var1, ActionEvent var2) {
      this.transfer(var1);
   }

   private void transfer(IntegrationEntity var1) {
      if (var1 instanceof IntegrationProject) {
         this.restoreCompleteProject();
      } else {
         RootFolder var2 = this.model.getTargetProject().getProject().getRootFolder(var1.getEntityType());
         this.worker.importItem(var1, var2);
      }
   }

   private void restoreCompleteProject() {
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
               ErrorLog.attention(ExportEntityAction.class, "Failed to restore the backup: " + var2x.getMessage(), var2x);
            }
         }
      });
   }
}
