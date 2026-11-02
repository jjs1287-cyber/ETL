package com.etlforge.integration.client.ui.admin.system;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.admin.server.UploadServerConfigurationFileCallback;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.UiJob;

final class UploadServerConfigurationFileJob extends UiJob {
   private final ServerAdministration serverApi;
   private final String text;
   private final UploadServerConfigurationFileJob.Callback callback = new UploadServerConfigurationFileJob.Callback();

   public UploadServerConfigurationFileJob(ServerAdministration var1, String var2) {
      super("Uploading etlforge.conf");
      this.serverApi = var1;
      this.text = var2;
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Uploading the file");

      try {
         File var1 = this.createTempFileForUpload();
         this.serverApi.uploadServerConfigurationFile(var1, this.callback);
      } catch (IOException var2) {
         this.callback.caught(var2);
      }
   }

   private File createTempFileForUpload() throws IOException {
      File var1 = ApplicationTempFolder.newFile(UUID.randomUUID().toString());
      var1.deleteOnExit();
      Files.write(this.text, var1, Charsets.UTF_8);
      return var1;
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private static class Callback implements UploadServerConfigurationFileCallback {
      private Callback() {
      }

      @Override
      public void fileWasUploaded() {
         EventQueue.invokeLater(
            new Runnable() {
               @Override
               public void run() {
                  String var1 = "etlforge.conf was saved successfully.\n\nIdentical content is left unchanged. Changes take effect when the affected ETLForge Windows services are restarted. Running operations have not been interrupted.";
                  Alert.info(var1, "Done");
               }
            }
         );
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(UploadServerConfigurationFileJob.class, "Failed to upload etlforge.conf: " + var1.getMessage(), var1);
      }
   }
}
