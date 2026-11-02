package org.etlforge.integration.server.engine.connector.netsuite.search;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteQueryPageSize;

public class NetSuiteSearchPreference {
   public static final NetSuiteSearchPreference testSearchPre = new NetSuiteSearchPreference("5");
   private final NetSuiteQueryPageSize queryPageSize;

   public NetSuiteSearchPreference(String var1) {
      this.queryPageSize = NetSuiteQueryPageSize.valueOf(var1);
   }

   public String toXmlString(ConnectorNetSuiteNamespaces var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("<searchPreferences xmlns=\"" + var1.getMessageNamespace() + "\"><bodyFieldsOnly>false</bodyFieldsOnly>");
      int var3 = this.queryPageSize.getPageSize();
      if (var3 > 0) {
         var2.append("<pageSize>" + var3 + "</pageSize>");
      }

      var2.append("</searchPreferences>");
      return var2.toString();
   }
}
