package com.etlforge.integration.client.ui.admin.project.backup;

import com.etlforge.integration.client.admin.project.GetListOfBackupsCallback;
import com.etlforge.integration.client.admin.project.ProjectBackupAdministration;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.ResultProducingUiJob;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.project.IntegrationProject;

class DownloadBackupListJob extends ResultProducingUiJob<ProjectBackupDescriptor[]> {
   private final IntegrationProject project;
   private final GetListOfBackupsCallback callback;

   public DownloadBackupListJob(IntegrationProject var1) {
      super(ProjectAdminBackupUiResources.getString("ListBackups.Job.Name"));
      this.project = var1;
      this.callback = new DownloadBackupListJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(ProjectAdminBackupUiResources.getFormattedMessage("ListBackups.Job.Run", this.project.getName()));
      ProjectBackupAdministration var1 = IntegrationServer.getInstance().getServerCall(ProjectBackupAdministration.class);
      var1.getListOfBackups(this.project.getID(), this.callback);
   }

   private final class Callback implements GetListOfBackupsCallback {
      private Callback() {
      }

      @Override
      public void backupDescriptorsDownloaded(ProjectBackupDescriptor[] var1) {
         DownloadBackupListJob.this.resultWasComputed(var1);
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(DownloadBackupListJob.class, var1.getMessage(), var1);
      }
   }
}
