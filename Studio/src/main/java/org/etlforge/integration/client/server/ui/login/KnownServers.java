package org.etlforge.integration.client.server.ui.login;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.server.KnownServer;

public interface KnownServers {
   KnownServers EMPTY = new KnownServers() {
      @Override
      public List<String> getServerNames() {
         return Lists.newArrayList();
      }

      @Override
      public KnownServer getServer(String var1) {
         return null;
      }
   };

   List<String> getServerNames();

   KnownServer getServer(String var1);
}
