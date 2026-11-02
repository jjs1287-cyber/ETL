package org.etlforge.integration.client.ui.admin.component.controlpanel;

import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.cli.CommandModule;
import org.etlforge.cli.ui.CommandConsole;

final class CommandActivator extends AbstractWindowElementResource {
   private final CommandConsole console;
   private final CommandModule commands;

   public CommandActivator(CommandConsole var1, CommandModule var2) {
      this.console = var1;
      this.commands = var2;
   }

   @Override
   public void create(WindowElement var1) {
      this.console.addCommand(this.commands);
   }

   @Override
   public void dispose(WindowElement var1) {
      this.console.removeCommand(this.commands);
   }
}
