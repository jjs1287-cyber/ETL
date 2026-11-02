package org.etlforge.integration.client.ui.admin.component.user2;

import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;

class ReadOnlyNotice {
   private final UserConsoleModel model;
   private final OverlayContainer overlayContainer;

   public ReadOnlyNotice(UserConsoleModel var1, OverlayContainer var2) {
      this.model = var1;
      this.overlayContainer = var2;
   }

   public void showReadOnlyMessage() {
      ServerInfo var1 = this.model.getCurrentServer();
      if (var1 != null) {
         String var2 = "Non-Admin User";
         String var3 = "<html>The current user does not have administration privileges on the server "
            + this.model.getCurrentServer().getName()
            + ".<br>No changes to the users and groups can be made.</html>";
         if (this.overlayContainer != null) {
            OverlayAlert var4 = new OverlayAlert(var3, var2);
            var4.setTitleIcon(AlertIcons.WARNING);
            var4.show(this.overlayContainer);
         } else {
            Alert.warning(var3, var2);
         }
      }
   }

   public void showNotSaveableMessage() {
      ServerInfo var1 = this.model.getCurrentServer();
      if (var1 != null) {
         String var2 = "Non-Admin User";
         String var3 = "<html>The current user does not have administration privileges on the server "
            + this.model.getCurrentServer().getName()
            + ".<br>Please log in as an administrator to save the changes.</html>";
         if (this.overlayContainer != null) {
            OverlayAlert var4 = new OverlayAlert(var3, var2);
            var4.setTitleIcon(AlertIcons.ERROR);
            var4.show(this.overlayContainer);
         } else {
            Alert.error(var3, var2);
         }
      }
   }
}
