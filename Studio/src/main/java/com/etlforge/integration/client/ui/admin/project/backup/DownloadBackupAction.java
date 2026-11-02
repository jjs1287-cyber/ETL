package com.etlforge.integration.client.ui.admin.project.backup;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.wait.WaitService;

class DownloadBackupAction extends AbstractSingleObjectSelectionAction<ProjectBackupDescriptor> {
   private final IntegrationProject project;
   private WaitService waitService = WaitService.NONE;

   public DownloadBackupAction(IntegrationProject var1) {
      super(ProjectBackupDescriptor.class, ProjectAdminBackupUiResources.getString("DownloadBackup.Action.Name"));
      this.putValue("ShortDescription", ProjectAdminBackupUiResources.getString("DownloadBackup.Action.ToolTip"));
      this.project = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1 != null ? var1 : WaitService.NONE;
   }

   protected void run(final ProjectBackupDescriptor var1, ActionEvent var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DownloadBackupAction.this.runImpl(var1);
         }
      });
   }

   private void runImpl(ProjectBackupDescriptor var1) {
      File var2 = new BackupDestinationProvider(this.project).getDestination();
      if (var2 != null) {
         DownloadBackupJob var3 = new DownloadBackupJob(var1, var2);
         var3.submit(this.waitService, 0);
      }
   }
}
