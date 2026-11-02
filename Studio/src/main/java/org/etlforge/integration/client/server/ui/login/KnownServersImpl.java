package org.etlforge.integration.client.server.ui.login;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.LatestLoginFirst;
import org.etlforge.integration.client.server.ServerInformationStore;

final class KnownServersImpl implements KnownServers {
   private final ImmutableMap<String, KnownServer> servers;

   public KnownServersImpl(ServerInformationStore var1) {
      List var2 = var1.getRegisteredServers();
      Collections.sort(var2, new LatestLoginFirst());
      this.servers = this.buildLookupMap(var2);
   }

   private ImmutableMap<String, KnownServer> buildLookupMap(List<KnownServer> var1) {
      LinkedHashMap var2 = Maps.newLinkedHashMap();

      for (KnownServer var4 : var1) {
         String var5 = getLookupName(var4.getServerInfo().getName());
         if (!var2.containsKey(var5)) {
            var2.put(var5, var4);
         }
      }

      return ImmutableMap.copyOf(var2);
   }

   private static String getLookupName(String var0) {
      String var1 = StringUtils.substringBefore(var0, ":");
      return var1.toLowerCase();
   }

   @Override
   public List<String> getServerNames() {
      ArrayList var1 = Lists.newArrayList();

      for (KnownServer var3 : this.servers.values()) {
         var1.add(var3.getServerInfo().getName());
      }

      return var1;
   }

   @Override
   public KnownServer getServer(String var1) {
      return this.servers.get(getLookupName(var1));
   }
}
