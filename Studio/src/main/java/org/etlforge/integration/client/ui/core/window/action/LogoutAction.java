package org.etlforge.integration.client.ui.core.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.server.ConnectionStatus;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.ui.action.ActionUtils;

public final class LogoutAction extends AbstractApplicationAction {
   public static final String ID = "action.core.Logout";
   private final ServerManager serverManager;

   public LogoutAction(ServerManager var1) {
      super("action.core.Logout", ClientWindowActionDictionary.DICTIONARY);
      this.serverManager = var1;
      this.setEnabled(false);
      var1.addServerConnectionListener(new LogoutAction.Listener());
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LogoutAction.this.logout();
         }
      });
   }

   private void logout() {
      ApplicationWorker.SafeRunnable var1 = new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            LogoutAction.this.serverManager.getLoginService().logout();
         }
      };
      Application.getWorker().submitForParallel(var1);
   }

   private final class Listener implements ServerConnectionListener {
      private Listener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         ActionUtils.setEnabledSafely(LogoutAction.this, var1.getStatus() == ConnectionStatus.CONNECTED);
      }
   }
}
