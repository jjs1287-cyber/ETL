package com.etlforge.integration.client.ui.project.download;

import java.io.File;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.data.project.IntegrationProject;

public class ProjectDownloadParams {
   public final IntegrationProject project;
   public final File destinationFolder;

   public ProjectDownloadParams(IntegrationProject var1, File var2) {
      this.project = var1;
      this.destinationFolder = var2;
   }

   public boolean isLocationOk() {
      boolean var1 = true;
      File var2 = new File(this.destinationFolder, this.project.getName());
      if (var2.exists()) {
         var1 = this.promptUserForOverWrite(var2);
      }

      return var1;
   }

   private boolean promptUserForOverWrite(File var1) {
      String var2 = "The project will be downloaded to the folder " + var1 + ".\nThis folder exists and will be overwritten.\n\nDo you want to continue?";
      String var3 = "Overwrite Existing Project?";
      Answer var4 = Ask.yesNoOrCancel(null, var2, var3, Alert.Option.NO, Alert.Type.WARNING);
      return var4.isYes();
   }
}
