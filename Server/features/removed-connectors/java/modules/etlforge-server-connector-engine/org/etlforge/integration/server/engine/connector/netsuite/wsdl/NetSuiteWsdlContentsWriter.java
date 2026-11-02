package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import java.io.File;
import java.util.Map.Entry;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;

public class NetSuiteWsdlContentsWriter {
   static void write(String var0, NetSuiteWsdlContents var1) {
      File var2 = new File(NetSuiteWsdlFile.getNetSuiteWsdlCacheFolder(), var0);
      write(var2, var1);
   }

   static void write(File var0, NetSuiteWsdlContents var1) {
      ConnectorExecutionSystem.createFolder(var0);

      for (Entry var3 : var1.getWsdlContents().entrySet()) {
         String var4 = (String)var3.getKey();
         File var5 = new File(var0, var4);
         ConnectorExecutionSystem.writeFileUTF8(var5, (String)var3.getValue());
      }
   }
}
