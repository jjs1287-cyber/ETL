package org.etlforge.integration.client.ui.admin.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.util.SystemActivityDisplayer;

public class ShowActivityConsoleAction extends AbstractApplicationAction {
   private final ApplicationWindow appWin;
   private final ServerConnectionService connectionService;
   private final SystemActivityDisplayer displayer;

   public ShowActivityConsoleAction(String var1, ApplicationWindow var2, ServerConnectionService var3, SystemActivityDisplayer var4) {
      super(var1, AdminActionDictionary.DICTIONARY);
      this.appWin = var2;
      this.connectionService = var3;
      this.displayer = var4;
   }

   @Override
   public final void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowActivityConsoleAction.this.openConsole();
         }
      });
   }

   private void openConsole() {
      this.displayer.display(this.appWin, this.connectionService);
   }
}
