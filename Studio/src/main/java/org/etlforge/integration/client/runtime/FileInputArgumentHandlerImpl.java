package org.etlforge.integration.client.runtime;

import java.io.File;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.integration.client.project.jitterpack.StartupJitterPackHandler;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class FileInputArgumentHandlerImpl implements FileInputArgumentHandler {
   private File jitterpak;
   private File projectFile;

   @Override
   public void setJitterpackToImport(File var1) {
      this.jitterpak = var1;
   }

   @Override
   public void setProjectToOpen(File var1) {
      this.projectFile = var1;
   }

   @Override
   public boolean isEmpty() {
      return this.jitterpak == null && this.projectFile == null;
   }

   @Override
   public void process() {
      if (this.jitterpak != null) {
         this.importJitterpak();
      } else if (this.projectFile != null) {
         throw new NotImplementedYetException();
      }
   }

   private void importJitterpak() {
      assert this.jitterpak != null;
      StartupJitterPackHandler var1 = this.getJitterPackHandler(this.jitterpak);
      UnpackCallback var2 = this.getUnpackCallback(this.jitterpak);
      var1.handle(this.jitterpak, var2);
   }

   private StartupJitterPackHandler getJitterPackHandler(File var1) {
      try {
         return (StartupJitterPackHandler)ClientPlatform.getAppContext().getBean("ui.StartupJitterpackHandler");
      } catch (Exception var3) {
         this.handleJitterpackConfigurationError(var1, var3);
         return null;
      }
   }

   private void handleJitterpackConfigurationError(File var1, Exception var2) {
      String var3 = "The ETLForge client has not been configured correctly to handle the jitterpak"
         + var1.getAbsolutePath()
         + " that was specified on the command-line.";
      FatalConfigurationError.throwError(9, this.getClass(), var2, var3);
   }

   private UnpackCallback getUnpackCallback(File var1) {
      try {
         return (UnpackCallback)ClientPlatform.getAppContext().getBean("ui.StartupJitterpackHandler.Callback");
      } catch (Exception var3) {
         this.handleJitterpackConfigurationError(var1, var3);
         return null;
      }
   }
}
