package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;

public final class ShowProjectConsoleAction extends ShowConsoleAction {
   public ShowProjectConsoleAction(ApplicationWindow var1, ServerConnectionService var2) {
      super("admin.action.ProjectConsole", var1, new ApplicationContextConsoleFactory("admin.ProjectConsole"));
      this.setConnectionService(var2);
   }
}
