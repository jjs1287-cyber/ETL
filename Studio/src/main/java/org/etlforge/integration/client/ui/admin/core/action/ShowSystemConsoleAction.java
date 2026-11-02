package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;

public final class ShowSystemConsoleAction extends ShowConsoleAction {
   public ShowSystemConsoleAction(ApplicationWindow var1, ServerConnectionService var2) {
      super("admin.action.SystemConsole", var1, new ApplicationContextConsoleFactory("admin.SystemConsole"));
      this.setConnectionService(var2);
   }
}
