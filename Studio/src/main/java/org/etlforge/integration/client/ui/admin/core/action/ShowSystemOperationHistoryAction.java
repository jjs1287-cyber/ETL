package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.util.SystemOperationHistoryDisplayer;

public final class ShowSystemOperationHistoryAction extends ShowActivityConsoleAction {
   public ShowSystemOperationHistoryAction(ApplicationWindow var1, SystemOperationHistoryDisplayer var2, ServerConnectionService var3) {
      super("admin.action.OperationHistory", var1, var3, var2);
   }
}
