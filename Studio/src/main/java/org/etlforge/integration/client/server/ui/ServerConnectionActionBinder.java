package org.etlforge.integration.client.server.ui;

import javax.swing.Action;
import org.etlforge.integration.client.server.ConnectionStatus;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;

public final class ServerConnectionActionBinder implements Disposable {
   private final ServerConnectionService connectionService;
   private final ServerConnectionActionBinder.Listener listener;
   private final Action action;

   public static Disposable install(ServerConnectionService var0, Action var1) {
      return new ServerConnectionActionBinder(var0, var1);
   }

   public ServerConnectionActionBinder(ServerConnectionService var1, Action var2) {
      this.connectionService = var1;
      this.listener = new ServerConnectionActionBinder.Listener();
      var1.addServerConnectionListener(this.listener);
      this.action = var2;
      this.updateAction(var1.getConnectionStatus() == ConnectionStatus.CONNECTED);
   }

   @Override
   public void dispose() {
      this.connectionService.removeServerConnectionListener(this.listener);
   }

   private void updateAction(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ServerConnectionActionBinder.this.action.setEnabled(var1);
         }
      });
   }

   private final class Listener implements ServerConnectionListener {
      private Listener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         ServerConnectionActionBinder.this.updateAction(var1.isConnected());
      }
   }
}
