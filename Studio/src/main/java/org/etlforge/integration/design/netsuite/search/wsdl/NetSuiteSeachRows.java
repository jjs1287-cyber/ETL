package org.etlforge.integration.design.netsuite.search.wsdl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteSeachRows implements Serializable {
   private final Map<String, NetSuiteSeachRow> searchRows = Maps.newHashMap();
   private final Set<String> advancedSearch = Sets.newHashSet();

   public void addAdvancedSearch(String var1) {
      String var2 = NetSuiteSearchObjectName.getObjectNameFromSearchAdvanced(var1);
      this.advancedSearch.add(var2);
   }

   public boolean hasAdvancedSearch(String var1) {
      return this.advancedSearch.contains(var1);
   }

   public void put(String var1, NetSuiteSeachRow var2) {
      this.searchRows.put(var1, var2);
   }

   public NetSuiteSeachRow getNSSeachRow(String var1) {
      return this.searchRows.get(var1);
   }
}
