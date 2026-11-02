package org.etlforge.application.ui;

import java.text.MessageFormat;
import javax.swing.JButton;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

public final class AboutDialog {
   private final UiProvider ui;

   public AboutDialog(UiProvider var1) {
      this.ui = var1;
   }

   public void display() {
      KongaDialog var1 = this.createDialog();
      var1.setLocationRelativeTo(null);
      var1.setVisible(true);
   }

   private KongaDialog createDialog() {
      KongaDialog var1 = new KongaDialog(UiUtils.getActiveFrame(), lookupDialogTitle(), true);
      var1.standardLayout(this.ui, KongaDialog.OK_ONLY);
      var1.setResizable(false);
      JButton var2 = var1.getOKButton();
      var1.getRootPane().setDefaultButton(var2);
      var1.setFocusedComponent(var2);
      return var1;
   }

   private static String lookupDialogTitle() {
      String var0 = ApplicationResources.STRINGS.getString("AboutDialog.Title");
      return MessageFormat.format(var0, ApplicationConfiguration.getProductName());
   }
}
