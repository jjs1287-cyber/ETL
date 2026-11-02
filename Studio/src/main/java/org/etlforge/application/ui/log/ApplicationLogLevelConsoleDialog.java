package org.etlforge.application.ui.log;

import java.awt.Window;
import org.etlforge.application.Application;
import org.etlforge.application.appdir.ApplicationDirectory;
import org.etlforge.application.appdir.LogDirectory;
import org.etlforge.ui.dialog.KongaDialog;

public final class ApplicationLogLevelConsoleDialog {
   private final ApplicationLogLevelConsole console;

   public static void openWithExistingApplicationLogs(Window var0) {
      createAndOpen(new ApplicationLogLevelConsole(), var0);
   }

   private static void createAndOpen(ApplicationLogLevelConsole var0, Window var1) {
      ApplicationLogLevelConsoleDialog var2 = new ApplicationLogLevelConsoleDialog(var0);
      var2.show(var1);
   }

   public ApplicationLogLevelConsoleDialog(ApplicationLogLevelConsole var1) {
      this.console = var1;
   }

   public void show(Window var1) {
      KongaDialog var2 = new KongaDialog(var1, "Configure Application Log Levels") {
         @Override
         protected void onApply() {
            this.applyLogLevels();
         }

         private void applyLogLevels() {
            ApplicationLogLevelConsoleDialog.this.console.apply();
            ApplicationLogLevelConsoleDialog.this.saveLogLevels();
         }

         @Override
         protected void onOk() {
            this.applyLogLevels();
            super.onOk();
         }
      };
      this.layoutDialog(var2);
      var2.manageLocation(this.getClass());
      var2.setVisible(true);
   }

   private void saveLogLevels() {
      ApplicationDirectory var1 = Application.getApplicationDirectory();
      if (var1 != null) {
         LogDirectory var2 = var1.getLogDirectory();
         this.console.saveLogLevels(var2);
      }
   }

   private void layoutDialog(KongaDialog var1) {
      var1.standardLayout(this.console, KongaDialog.OK_CANCEL_APPLY);
   }
}
