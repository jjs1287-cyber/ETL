package org.etlforge.integration.client.ui.admin.core.action;

import com.google.common.base.Supplier;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.component.jms.JmsEngineConsolePage;

public final class ShowJmsEngineConsoleAction extends ShowConsoleAction {
   public ShowJmsEngineConsoleAction(ApplicationWindow var1, final Admin var2, ServerConnectionService var3) {
      super("admin.action.JmsConsole", var1, new Supplier<Editor>() {
         public Editor get() {
            return new JmsEngineConsolePage(var2);
         }
      });
      this.setConnectionService(var3);
   }
}
