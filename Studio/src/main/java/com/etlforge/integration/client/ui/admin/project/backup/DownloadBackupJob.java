package com.etlforge.integration.client.ui.admin.project.backup;

import com.google.common.io.Files;
import com.etlforge.integration.client.admin.server.DownloadServerFileCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;

class DownloadBackupJob extends UiJob {
   private final ProjectBackupDescriptor backup;
   private final File destinationFile;
   private final DownloadBackupJob.Callback callback;

   public DownloadBackupJob(ProjectBackupDescriptor var1, File var2) {
      super(ProjectAdminBackupUiResources.getString("DownloadBackup.Job.Name"));
      this.backup = var1;
      this.destinationFile = var2;
      this.callback = new DownloadBackupJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(ProjectAdminBackupUiResources.getString("DownloadBackup.Job.Run"));
      ServerAdministration var1 = IntegrationServer.getInstance().getServerCall(ServerAdministration.class);
      var1.downloadServerFile(this.backup.getFilePath(), this.callback);
   }

   private class Callback implements DownloadServerFileCallback {
      private Callback() {
      }

      @Override
      public void fileWasDownloaded(File var1) {
         try {
            Files.copy(var1, DownloadBackupJob.this.destinationFile);
         } catch (IOException var3) {
            ErrorLog.attention(DownloadBackupJob.class, "Failed to create the file " + DownloadBackupJob.this.destinationFile.getAbsolutePath(), var3);
         }
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(DownloadBackupJob.class, var1.getMessage(), var1);
      }
   }
}
