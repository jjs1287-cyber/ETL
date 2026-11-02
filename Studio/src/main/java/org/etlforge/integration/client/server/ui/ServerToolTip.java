package org.etlforge.integration.client.server.ui;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;
import org.etlforge.ui.util.KeyValueToolTip;

public final class ServerToolTip {
   private final String tooltip;

   public ServerToolTip(KnownServer var1) {
      this.tooltip = createToolTip(var1);
   }

   public ServerToolTip(ServerInfo var1) {
      this.tooltip = createToolTip(var1);
   }

   public String get() {
      return this.tooltip;
   }

   private static String createToolTip(KnownServer var0) {
      ServerInfo var1 = var0.getServerInfo();
      Map var2 = getServerInfoMap(var1);
      putPair(var2, "ServerToolTip.LastConnected", var0.getDateOfLatestLogin());
      putPair(var2, "ServerToolTip.User", var0.getLatestUser());
      return KeyValueToolTip.createTable(var2);
   }

   private static String createToolTip(ServerInfo var0) {
      Map var1 = getServerInfoMap(var0);
      return KeyValueToolTip.createTable(var1);
   }

   private static Map<Object, Object> getServerInfoMap(ServerInfo var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      putPair(var1, "ServerToolTip.Name", var0.getName());
      putPair(var1, "ServerToolTip.Version", var0.getVersion());
      putPair(var1, "ServerToolTip.ServerID", var0.getGuid());
      return var1;
   }

   private static void putPair(Map<Object, Object> var0, String var1, Object var2) {
      var0.put(ServerUiResources.getString(var1), var2);
   }
}
