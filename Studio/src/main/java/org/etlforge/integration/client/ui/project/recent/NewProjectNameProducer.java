package org.etlforge.integration.client.ui.project.recent;

import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.ui.project.ProjectNameInput;

final class NewProjectNameProducer {
   public static String askForNewName(ProjectLocation var0) {
      ProjectNameInput var1 = new ProjectNameInput(Strings.get("Recent.Rename.Title"), Strings.get("Recent.Rename.Caption"));
      var1.setInitialValue(var0.getProjectName());
      String var2 = var1.showInDialog(null);
      return isNewName(var0, var2) ? var2 : null;
   }

   private static boolean isNewName(ProjectLocation var0, String var1) {
      if (var1 == null) {
         return false;
      }

      boolean var2 = !var1.equals(var0.getProjectName());
      if (var2 && SystemUtils.IS_OS_WINDOWS) {
         boolean var3 = var1.equalsIgnoreCase(var0.getProjectName());
         if (var3) {
            Alert.info(Strings.get("Recent.Rename.CaseOnly.Message"), Strings.get("Recent.Rename.CaseOnly.Title"));
            return false;
         }
      }

      return var2;
   }

   private NewProjectNameProducer() {
   }
}
