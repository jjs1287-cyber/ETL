package org.etlforge.integration.server.engine.connector.netsuite.search;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;

public class NetSuiteSearchNames {
   private final boolean isSearchRow;
   private final ConnectorNetSuiteNamespaces ns;

   public NetSuiteSearchNames(boolean var1, ConnectorNetSuiteNamespaces var2) {
      this.isSearchRow = var1;
      this.ns = var2;
   }

   public boolean isRecordListElement(String var1, String var2) {
      boolean var3 = this.isSearchRow ? var2.equals("searchRowList") : var2.equals("recordList");
      return var3 ? var1.equals(this.getRecordResponseNamespace()) : false;
   }

   public String getRecordResponseNamespace() {
      return this.ns.getRecordResponseElementNamespace();
   }

   public String getRecordLocalName() {
      return this.isSearchRow ? "searchRow" : "record";
   }
}
