package org.etlforge.integration.client.ui.jitterpack;

import java.io.File;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;

final class DestinationChecker {
   public boolean isLocationOk(ImportParameters var1, JitterPackDescriptor var2, File var3) {
      String var4 = this.getFolderName(var1, var2);
      File var5 = new File(var3, var4);
      return var5.exists() ? this.canDestinationBeOverwritten(var5) : true;
   }

   private String getFolderName(ImportParameters var1, JitterPackDescriptor var2) {
      String var3 = var1.getNewProjectName();
      if (var3 == null) {
         var3 = var2.getFolderName();
      }

      return var3;
   }

   private boolean canDestinationBeOverwritten(File var1) {
      String var2 = getOverwritePrompt(var1);
      Answer var3 = Ask.yesOrNo(null, var2, Strings.get("Prompt.Overwrite.Title"), Alert.Option.NO, Alert.Type.WARNING);
      return var3.isYes();
   }

   private static String getOverwritePrompt(File var0) {
      String var1 = var0.getAbsolutePath();
      String var2 = var0.isDirectory() ? Strings.get("Folder.LowerCase") : Strings.get("File.LowerCase");
      return Strings.format("Prompt.Overwrite", var1, var2);
   }
}

