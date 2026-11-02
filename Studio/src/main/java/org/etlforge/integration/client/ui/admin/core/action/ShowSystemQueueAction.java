package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.util.SystemQueueDisplayer;

public final class ShowSystemQueueAction extends ShowActivityConsoleAction {
   public ShowSystemQueueAction(ApplicationWindow var1, OperationQueueProvider var2, ServerConnectionService var3) {
      super("admin.action.OperationQueue", var1, var3, new SystemQueueDisplayer(var2));
   }
}
