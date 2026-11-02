package org.etlforge.integration.client.project.deploy;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.deploy.serverapi.IntegrationProjectDeployer;
import org.etlforge.integration.client.deploy.serverapi.JitterPackServerBackup;
import org.etlforge.integration.client.deploy.serverapi.UploadJitterPackCallback;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;

final class ProjectBackupSender {
   private static final ExecutorService worker = Executors.newFixedThreadPool(1, new ThreadFactory() {
      @Override
      public Thread newThread(Runnable var1) {
         Thread var2 = Executors.defaultThreadFactory().newThread(var1);
         var2.setName(ProjectBackupSender.class.getName() + " - Worker Thread");
         var2.setPriority(3);
         return var2;
      }
   });
   private final ProjectManager manager;

   public ProjectBackupSender(ProjectManager var1) {
      this.manager = var1;
   }

   public void uploadBackup(final IntegrationProject var1, final File var2, final String var3) {
      Runnable var4 = new Runnable() {
         @Override
         public void run() {
            try {
               JitterPack var1x = ProjectBackupSender.this.createJitterPack(var2);
               if (var1x != null) {
                  ProjectBackupSender.this.uploadJitterPack(var1, var1x, var3);
               }
            } catch (Exception var2x) {
               ProjectBackupSender.this.logFailure(var2x);
            }
         }
      };
      worker.execute(var4);
   }

   private JitterPack createJitterPack(File var1) {
      try {
         IntegrationProjectLock.acquire();
         File var2 = this.createServerBackup(var1);
         String var3 = var2.getName();
         File var4 = this.getJitterPackFile(var3);
         return this.manager.createJitterPack(var3, var2, var4, false);
      } catch (Exception var10) {
         this.logFailure(var10);
         return null;
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private File createServerBackup(File var1) {
      String var2 = var1.getName();
      int var3 = var2.lastIndexOf(" [");
      if (var3 >= 0) {
         String var4 = var2.substring(0, var3);
         File var5 = ApplicationTempFolder.newFolder(var4, true);

         try {
            FileUtils.cleanDirectory(var5);
            FileUtils.copyDirectory(var1, var5);
            return var5;
         } catch (IOException var7) {
            ErrorLog.log(
               ProjectBackupSender.class,
               "Failed to create a trimmed down version of the local backup to send to the server. We will send the original client backup instead.",
               var7
            );
         }
      }

      return var1;
   }

   private void uploadJitterPack(IntegrationProject var1, final JitterPack var2, String var3) {
      if (!Thread.currentThread().isInterrupted()) {
         IntegrationServer var4 = IntegrationServer.getInstance();
         IntegrationProjectDeployer var5 = var4.getServerCall(IntegrationProjectDeployer.class);
         JitterPackServerBackup var6 = new JitterPackServerBackup(var1.getID(), var2.getFile(), var3);
         var5.uploadJitterPack(var6, new UploadJitterPackCallback() {
            @Override
            public void uploadComplete() {
               ProjectBackupSender.this.doCleanup(var2);
            }

            @Override
            public void cancelled() {
               ProjectBackupSender.this.doCleanup(var2);
            }

            @Override
            public void caught(Throwable var1) {
               ProjectBackupSender.this.logFailure(var1);
               ProjectBackupSender.this.doCleanup(var2);
            }
         });
      }
   }

   private File getJitterPackFile(String var1) throws IOException {
      String var2 = ApplicationTempFolder.getCanonicalPath();
      File var3 = new File(var2, UUID.randomUUID().toString());
      return new File(var3, var1 + ".jpk");
   }

   private void doCleanup(JitterPack var1) {
      try {
         File var2 = var1.getFile().getParentFile();
         FileUtils.deleteDirectory(var2);
      } catch (IOException var3) {
         ErrorLog.log(ProjectBackupSender.class, "Failed to delete temporary local copy of the Jitterpak \"" + var1.getFile().getAbsolutePath() + "\"", var3);
      }
   }

   private void logFailure(Throwable var1) {
      ErrorLog.log(ProjectBackupSender.class, "Failed to send a backup Jitterpak of the project to the server.", var1);
      DeployLog.LOG.info("Failed to send the Jitterpak backup to the server. Check the ErrorLog for more details.");
   }
}
