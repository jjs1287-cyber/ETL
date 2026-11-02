package org.etlforge.integration.client.ui.core.main;

import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.integration.client.ui.icons.AboutIcons;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

public final class MainAuthorDialog {
   public static void display() {
      KongaDialog var0 = createDialog();
      var0.setVisible(true);
   }

   private static KongaDialog createDialog() {
      KongaDialog var0 = new KongaDialog(UiUtils.getActiveWindow(), "I made this!");
      Icon var1 = AboutIcons.MAIN_AUTHOR;
      JLabel var2 = new JLabel(var1);
      var0.standardLayout(var2, KongaDialog.CLOSE_ONLY);
      var0.setResizable(false);
      var0.setLocationRelativeTo(null);
      return var0;
   }

   private MainAuthorDialog() {
   }
}
