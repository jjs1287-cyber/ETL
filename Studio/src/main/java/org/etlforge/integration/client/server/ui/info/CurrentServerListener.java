package org.etlforge.integration.client.server.ui.info;

import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.ui.DelayedJob;

final class CurrentServerListener {
   private final ServerConnectionService connectionService;
   private final KnownServersViewModel model;
   private final KnownServersViewController controller;
   private final ServerConnectionListener listener = new CurrentServerListener.Listener();

   public CurrentServerListener(ServerConnectionService var1, KnownServersViewModel var2, KnownServersViewController var3) {
      this.connectionService = var1;
      this.model = var2;
      this.controller = var3;
   }

   public void activate() {
      this.connectionService.addServerConnectionListener(this.listener);
   }

   public void dispose() {
      this.connectionService.removeServerConnectionListener(this.listener);
   }

   private class Listener implements ServerConnectionListener {
      private Listener() {
      }

      @Override
      public void serverConnectionChanged(final ServerConnectionEvent var1) {
         DelayedJob.createAndSchedule(250, new Runnable() {
            @Override
            public void run() {
               if (var1.isConnected()) {
                  CurrentServerListener.this.controller.updateListOfServers();
                  CurrentServerListener.this.model.setConnectedTo(var1.getServerInfo());
               } else {
                  CurrentServerListener.this.model.setConnectedTo(null);
               }
            }
         });
      }
   }
}
