package com.etlforge.integration.client.ui.admin.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.core.action.ApplicationContextConsoleFactory;
import org.etlforge.integration.client.ui.admin.core.action.ShowConsoleAction;

public final class ShowServerLogsConsoleAction extends ShowConsoleAction {
   public ShowServerLogsConsoleAction(ApplicationWindow var1, ServerConnectionService var2) {
      super("admin.action.ServerLogs", var1, EnterpriseAdminActionDictionary.DICTIONARY, new ApplicationContextConsoleFactory("admin.ServerLogConsole"));
      this.setConnectionService(var2);
   }
}
