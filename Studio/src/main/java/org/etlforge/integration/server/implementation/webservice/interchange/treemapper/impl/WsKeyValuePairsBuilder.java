package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

final class WsKeyValuePairsBuilder {
   public static WsKeyValuePairs build(Map<String, String> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : var0.entrySet()) {
         var1.add(new WsKeyValue((String)var3.getKey(), (String)var3.getValue()));
      }

      WsKeyValuePairs var4 = new WsKeyValuePairs();
      var4.setKeyValues((WsKeyValue[]) var1.toArray(new WsKeyValue[var1.size()]));
      return var4;
   }

   private WsKeyValuePairsBuilder() {
   }
}
