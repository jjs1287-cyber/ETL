package org.etlforge.integration.server.engine.connector.netsuite.search;

import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.process.NetSuiteRecordRefWrapper;
import org.etlforge.integration.server.engine.connector.netsuite.process.NetSuiteRecordResponse;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;
import org.etlforge.io.FileUtils;

public class NetSuiteSearchOperation {
   private static final String searchOperation = "search";
   private final NetSuitePassport passport;
   private final NetSuiteSearchPreference preference;
   private final boolean isSearchRow;
   private final ConnectorNetSuiteNamespaces ns;

   public NetSuiteSearchOperation(NetSuitePassport var1, NetSuiteSearchPreference var2, boolean var3) {
      this.passport = var1;
      this.preference = var2;
      this.isSearchRow = var3;
      this.ns = NetSuiteExecutionContext.INSTANCE.getExecution(var1).getConnectorNetSuiteNamespaces();
   }

   public String testQuery(String var1) {
      NetSuiteSoapServiceExecution var2 = new NetSuiteSoapServiceExecution(this.passport, this.preference.toXmlString(this.ns));
      return var2.executeString("search", var1, true);
   }

   public void search(NetSuiteRecordRefWrapper var1, NetSuiteObject var2, String var3, String var4) {
      String var5 = FileUtils.getPostfixFilenameXml(var4, "_ns");
      this.searchImpl(var3, var5);
      NetSuiteRecordResponse.transform(var1, this.ns, var2, var5, var4, this.isSearchRow);
   }

   private void searchImpl(String var1, String var2) {
      this.searchServiceCall(var1, var2);
      NetSuiteSearchResponse var3 = NetSuiteSearchResponse.getNetSuiteSearchResponseFile(var2);
      if (var3.totalPages >= 2) {
         NetSuiteSearchMoreWithIdOperation var4 = new NetSuiteSearchMoreWithIdOperation(this.passport);

         for (int var5 = 2; var5 <= var3.totalPages; var5++) {
            var4.execute(var3.getSearchId(), var5, var1, var2);
         }

         NetSuiteSearchResponseMerger var6 = new NetSuiteSearchResponseMerger(var3.totalPages, var2, this.isSearchRow, this.ns);
         var6.merge();
      }
   }

   private void searchServiceCall(String var1, String var2) {
      NetSuiteSoapServiceExecution var3 = new NetSuiteSoapServiceExecution(this.passport, this.preference.toXmlString(this.ns));
      var3.executeFile("search", var1, var2);
   }
}
