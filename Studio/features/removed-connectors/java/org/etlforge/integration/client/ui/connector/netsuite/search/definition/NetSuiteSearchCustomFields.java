package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectCustomFields;

public class NetSuiteSearchCustomFields {
   private final NetSuiteSearchCustomObject customObject;
   private final Map<String, NetSuiteSearchObjectCustomFields> customFields = Maps.newHashMap();

   public NetSuiteSearchCustomFields(NetSuiteSearchCustomObject var1) {
      this.customObject = var1;
   }

   public NetSuiteSearchCustomObject getNetSuiteSearchCustomObject() {
      return this.customObject;
   }

   public void put(String var1, NetSuiteSearchObjectCustomFields var2) {
      this.customFields.put(var1, var2);
   }

   public NetSuiteSearchObjectCustomFields get(String var1) {
      return this.customFields.get(var1);
   }

   public NetSuiteSearchObjectCustomFields getOnlyOne() {
      if (this.customFields.size() == 1) {
         Iterator var1 = this.customFields.entrySet().iterator();
         if (var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            return (NetSuiteSearchObjectCustomFields)var2.getValue();
         }
      }

      return null;
   }
}
