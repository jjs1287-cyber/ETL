package org.etlforge.integration.server.backend.webservice.jdbc;

import com.google.common.collect.Maps;
import java.util.Map;

final class KeyValues {
   private final Map<String, String> map = Maps.newHashMap();

   public KeyValues(WsKeyValue[] var1) {
      if (var1 != null) {
         for (WsKeyValue var5 : var1) {
            this.map.put(var5.getKey(), var5.getValue());
         }
      }
   }

   public String getString(String var1, String var2) {
      String var3 = this.map.get(var1);
      return var3 == null ? var2 : var3;
   }

   public boolean getBoolean(String var1, boolean var2) {
      String var3 = this.map.get(var1);
      return var3 == null ? var2 : var3.equals("1") || var3.equalsIgnoreCase("true");
   }
}
