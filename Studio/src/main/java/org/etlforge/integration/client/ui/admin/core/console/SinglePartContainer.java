package org.etlforge.integration.client.ui.admin.core.console;

import javax.swing.JComponent;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.ui.UiCanvas;

final class SinglePartContainer implements AdminConsolePartContainer {
   private final AdminConsolePart part;

   public SinglePartContainer(AdminConsolePart var1) {
      this.part = var1;
   }

   @Override
   public AdminConsolePart getActivePart() {
      return this.part;
   }

   @Override
   public JComponent getToolBarContribution() {
      return null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.part.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.part.requestFocus();
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
      this.part.serverConnectionChanged(var1);
   }
}
