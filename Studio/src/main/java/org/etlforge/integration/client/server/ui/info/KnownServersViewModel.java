package org.etlforge.integration.client.server.ui.info;

import java.util.List;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class KnownServersViewModel extends AbstractPropertyBasedModel {
   public static final String SERVERS = "servers";
   public static final String CONNECTED_TO = "connected_to";
   private final ListProperty<KnownServer> servers = this.newListProperty("servers");
   private final Property<ServerInfo> connectedTo = this.newProperty("connected_to");

   public void setKnownServers(List<KnownServer> var1) {
      this.servers.set(var1);
   }

   public List<KnownServer> getKnownServers() {
      return this.servers.get();
   }

   public void setConnectedTo(ServerInfo var1) {
      this.connectedTo.set(var1);
   }

   public ServerInfo getConnectedTo() {
      return this.connectedTo.get();
   }

   public boolean isConnectedTo(KnownServer var1) {
      ServerInfo var2 = this.connectedTo.get();
      return var2 != null && var2.getGuid().equals(var1.getServerInfo().getGuid());
   }

   public void removeServers(List<KnownServer> var1) {
      List var2 = this.getKnownServers();
      boolean var3 = var2.removeAll(var1);
      if (var3) {
         this.setKnownServers(var2);
      }
   }
}
