package org.etlforge.integration.client.ui.admin.core.console;

import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.label.KongaLabel;

final class NoPartContainer implements AdminConsolePartContainer {
   private final String consoleName;

   public NoPartContainer(String var1) {
      this.consoleName = var1;
   }

   @Override
   public AdminConsolePart getActivePart() {
      return null;
   }

   @Override
   public JComponent getToolBarContribution() {
      return null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaLabel var2 = TextStyles.ErrorInfoText
         .makeLabel(
            "<html>Configuration error: The " + this.consoleName + " configuration is incorrect.<br><br>This is a bug. Please report it to ETLForge Support."
         );
      var2.setFont(var2.getFont().deriveFont(20.0F));
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
   }
}
