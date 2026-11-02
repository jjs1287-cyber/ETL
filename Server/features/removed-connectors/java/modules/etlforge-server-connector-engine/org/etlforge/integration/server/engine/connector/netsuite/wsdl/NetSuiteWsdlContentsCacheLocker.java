package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import com.google.common.collect.Maps;
import java.util.Map;

public class NetSuiteWsdlContentsCacheLocker {
   private final Map<String, Object> lockers = Maps.newHashMap();

   public synchronized Object getLocker(String var1) {
      Object var2 = this.lockers.get(var1);
      if (var2 == null) {
         var2 = new Object();
         this.lockers.put(var1, var2);
      }

      return var2;
   }
}
