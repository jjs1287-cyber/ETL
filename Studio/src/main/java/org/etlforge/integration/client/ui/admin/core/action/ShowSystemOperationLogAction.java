package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.util.SystemOperationLogDisplayer;

public final class ShowSystemOperationLogAction extends ShowActivityConsoleAction {
   public ShowSystemOperationLogAction(ApplicationWindow var1, OperationLogProvider var2, ServerConnectionService var3) {
      super("admin.action.OperationLog", var1, var3, new SystemOperationLogDisplayer(var2));
   }
}
