package org.etlforge.integration.loadsource.client;

import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.loadsource.client.resources.LoadSourceResources;
import org.etlforge.system.callback.Receiver;

final class DialogSourceDataFileInputProvider extends WizardStyleDialog {
   private final ProjectSourceDataFileInputUi ui;

   public DialogSourceDataFileInputProvider(ApplicationWindow var1, ProjectSourceDataFileInputUi var2) {
      super(var1.getFrame(), LoadSourceResources.getString("SourceDataFileProvider.Title"));
      this.ui = var2;
      this.prepareUi();
      this.getDefaultLayout().center(var2);
      this.pack();
      this.setResizable(false);
      this.manageLocation(ProjectSourceDataFileInputProvider.class);
      this.setOkButtonAsDefault();
   }

   private void prepareUi() {
      this.ui.setFinalSelectionMonitor(new Receiver<Void>() {
         public void handle(Void var1) {
            DialogSourceDataFileInputProvider.this.onOk();
         }
      });
   }

   public LoadSourceDataFileInput selectInput() {
      this.setVisible(true);
      return this.wasOkPressed() ? this.ui.getSelectedFile() : null;
   }

   @Override
   protected void onDialogOpened() {
      this.ui.requestFocus();
   }

   @Override
   protected void onDialogClose() {
      try {
         super.onDialogClose();
      } finally {
         this.ui.dispose();
      }
   }
}
