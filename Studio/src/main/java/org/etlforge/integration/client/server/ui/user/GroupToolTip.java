package org.etlforge.integration.client.server.ui.user;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;
import org.etlforge.integration.user.Group;
import org.etlforge.ui.util.KeyValueToolTip;

public final class GroupToolTip {
   private final String tooltip;

   public GroupToolTip(Group var1) {
      this.tooltip = createToolTip(var1);
   }

   public String get() {
      return this.tooltip;
   }

   private static String createToolTip(Group var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      putPair(var1, "GroupToolTip.Name", var0.getName());
      putPair(var1, "GroupToolTip.Description", var0.getDescription());
      return KeyValueToolTip.createTable(var1);
   }

   private static void putPair(Map<Object, Object> var0, String var1, Object var2) {
      var0.put(ServerUiResources.getString(var1), var2);
   }
}
