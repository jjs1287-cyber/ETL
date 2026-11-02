package org.etlforge.integration.client.ui.admin.component.user2;

import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public class InvalidUserDataAlert {
   private final String message;
   private final OverlayContainer container;

   public InvalidUserDataAlert(InvalidUserDataException var1) {
      this(var1, null);
   }

   public InvalidUserDataAlert(InvalidUserDataException var1, OverlayContainer var2) {
      this.message = var1.getMessage();
      this.container = var2;
   }

   public void show() {
      String var1 = "Invalid Data";
      if (this.container != null) {
         OverlayAlert var2 = new OverlayAlert(this.message, var1);
         var2.setTitleIcon(AlertIcons.ERROR);
         var2.show(this.container);
      } else {
         Alert.error(this.message, var1);
      }
   }
}
