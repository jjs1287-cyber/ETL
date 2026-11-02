package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;

public final class ShowUserConsoleAction extends ShowConsoleAction {
   public ShowUserConsoleAction(ApplicationWindow var1, ServerConnectionService var2) {
      super("admin.action.UserConsole", var1, new ApplicationContextConsoleFactory("admin.UserConsole"));
      this.setConnectionService(var2);
   }
}
