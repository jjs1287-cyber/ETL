package org.etlforge.integration.server.engine.connector.netsuite.search;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;
import org.etlforge.io.FileUtils;

public class NetSuiteSearchMoreWithIdOperation {
   private final NetSuitePassport passport;

   public NetSuiteSearchMoreWithIdOperation(NetSuitePassport var1) {
      this.passport = var1;
   }

   public static String getNewFilename(String var0, Integer var1) {
      return FileUtils.getPostfixFilenameXml(var0, "_Id" + var1.toString());
   }

   public void execute(String var1, int var2, String var3, String var4) {
      NetSuiteSearchMoreWithId var5 = new NetSuiteSearchMoreWithId(var1, var2);
      this.execute(var5, var3, var4);
   }

   public void execute(NetSuiteSearchMoreWithId var1, String var2, String var3) {
      String var4 = getNewFilename(var2, var1.pageIndex);
      String var5 = getNewFilename(var3, var1.pageIndex);
      ConnectorNetSuiteNamespaces var6 = NetSuiteExecutionContext.INSTANCE.getExecution(this.passport).getConnectorNetSuiteNamespaces();
      var1.toXmlFile(var6, var4);
      NetSuiteSoapServiceExecution var7 = new NetSuiteSoapServiceExecution(this.passport);
      var7.executeFile(var1.getOperationName(), var4, var5);
   }
}
