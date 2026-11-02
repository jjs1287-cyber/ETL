package org.etlforge.integration.client.server;

import java.util.Collections;
import java.util.List;
import org.etlforge.integration.common.server.ServerGuid;

public interface ServerInformationStore {
   ServerInformationStore EMPTY = new ServerInformationStore() {
      @Override
      public List<KnownServer> getRegisteredServers() {
         return Collections.emptyList();
      }

      @Override
      public boolean removeServerRegistration(ServerGuid var1) {
         return false;
      }

      @Override
      public void storeNewLogin(ServerInfo var1, String var2, String var3, boolean var4) {
      }
   };

   List<KnownServer> getRegisteredServers();

   boolean removeServerRegistration(ServerGuid var1);

   void storeNewLogin(ServerInfo var1, String var2, String var3, boolean var4);
}
