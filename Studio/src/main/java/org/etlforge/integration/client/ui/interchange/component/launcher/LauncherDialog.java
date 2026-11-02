package org.etlforge.integration.client.ui.interchange.component.launcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class LauncherDialog {
   public static void publish(InterchangeLauncher var0) {
      var0.wakeUp();
      JFrame var1 = var0.getWindow().getFrame();
      LauncherDialog.Dialog var2 = createDialog(var1, var0);
      var2.setVisible(true);
   }

   private static LauncherDialog.Dialog createDialog(JFrame var0, InterchangeLauncher var1) {
      return new LauncherDialog.Dialog(var1, var0, Strings.get("Launcher.Dialog.Title"));
   }

   private LauncherDialog() {
   }

   private static class Dialog extends KongaDialog {
      private final InterchangeLauncher launcher;
      private final UiProvider ui;
      private final ActionListener listener;

      public Dialog(InterchangeLauncher var1, JFrame var2, String var3) {
         super(var2, var3, false);
         this.launcher = var1;
         this.listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               Dialog.this.dispose();
            }
         };
         this.ui = var1.getUi();
         var1.addLaunchListener(this.listener);
         this.layoutDialog();
         this.manageLocation();
      }

      @Override
      protected void onDialogOpened() {
         super.onDialogOpened();
         this.ui.requestFocus();
      }

      @Override
      protected void onDialogClose() {
         super.onDialogClose();
         this.launcher.removeLaunchListener(this.listener);
         this.launcher.hibernate();
      }

      private void layoutDialog() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder();
         var1.center(this.ui).asContentPaneFor(this);
         this.pack();
      }
   }
}
