package org.etlforge.integration.design.netsuite.search.wsdl;

import java.io.Serializable;

public class NetSuiteDefinitionSearch implements Serializable {
   private final NetSuiteSearchWsdl searchWsdl;
   private final NetSuiteSeachRows searchRows;

   public NetSuiteDefinitionSearch(NetSuiteSearchWsdl var1, NetSuiteSeachRows var2) {
      this.searchWsdl = var1;
      this.searchRows = var2;
   }

   public final NetSuiteSeachRows getNSSeachRows() {
      return this.searchRows;
   }

   public final NetSuiteSearchWsdl getNetSuiteSearchWsdl() {
      return this.searchWsdl;
   }
}
