package org.etlforge.integration.client.ui.admin.component.proxy;

import java.awt.EventQueue;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;

final class NonAdminMessage {
   private final OverlayContainer overlayContainer;
   private OverlayAlert alert;

   public NonAdminMessage(OverlayContainer var1) {
      this.overlayContainer = var1;
   }

   public void show(final ServerInfo var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NonAdminMessage.this.showImpl(var1);
         }
      });
   }

   private void showImpl(ServerInfo var1) {
      if (this.alert == null) {
         String var2 = "Non-Admin User";
         String var3 = "<html>The current user does not have administration privileges on the server "
            + var1.getName()
            + ".<br>Only administrators can change the server's proxy settings.</html>";
         this.alert = new OverlayAlert(var3, var2);
         this.alert.setTitleIcon(AlertIcons.WARNING);
         this.alert.show(this.overlayContainer);
      }
   }

   public void hide() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NonAdminMessage.this.hideImpl();
         }
      });
   }

   private void hideImpl() {
      if (this.alert != null) {
         this.alert.hide();
         this.alert = null;
      }
   }
}
