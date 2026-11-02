package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;

public final class ShowSchedulerConsoleAction extends ShowConsoleAction {
   public ShowSchedulerConsoleAction(ApplicationWindow var1, ServerConnectionService var2) {
      super("admin.action.SchedulerConsole", var1, new ApplicationContextConsoleFactory("admin.SchedulerConsole"));
      this.setConnectionService(var2);
   }
}
