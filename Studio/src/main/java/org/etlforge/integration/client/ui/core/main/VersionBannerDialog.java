package org.etlforge.integration.client.ui.core.main;

import java.awt.Window;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.core.window.VersionBanner;
import org.etlforge.integration.client.ui.icons.AboutIcons;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.label.KongaLabel;

final class VersionBannerDialog {
   private KongaDialog dialog;

   public VersionBannerDialog(Window var1, VersionBanner var2) {
      this.dialog = new KongaDialog(var1, "Current Version");
      this.createLayout(var2);
      this.dialog.pack();
      this.dialog.setResizable(false);
      this.dialog.setLocationRelativeTo(null);
      this.dialog.setCloseButtonAsDefault();
   }

   private void createLayout(VersionBanner var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      this.layoutCaption(var2, var1);
      this.layoutImage(var2, var1);
      this.dialog.standardLayout(var2, KongaDialog.CLOSE_ONLY);
   }

   private void layoutCaption(BorderLayoutBuilder var1, VersionBanner var2) {
      KongaLabel var3 = TextStyles.MediumCaption.makeLabel(this.createCaptionString(var2));
      var3.setToolTipText(var2.getToolTip());
      var1.north(var3);
   }

   private String createCaptionString(VersionBanner var1) {
      StringBuilder var2 = new StringBuilder("Version: ");
      var2.append(ClientInfo.getCurrentVersion().getPatchLevelString());
      var2.append(" - ");
      var2.append(var1.getCodeName());
      return var2.toString();
   }

   private void layoutImage(BorderLayoutBuilder var1, VersionBanner var2) {
      JLabel var3 = new JLabel(this.getImage(var2));
      var3.setToolTipText(var2.getToolTip());
      var1.center(var3);
   }

   private Icon getImage(VersionBanner var1) {
      Icon var2 = null;
      String var3 = var1.getImageName();
      if (var3 != null) {
         var2 = AboutIcons.loadIcon(var3);
         if (var2 == null) {
            var2 = ClientIcons.APPLICATION_32;
         }
      }

      return var2;
   }

   public void show() {
      this.dialog.setVisible(true);
      this.dialog.dispose();
      this.dialog = null;
   }
}
