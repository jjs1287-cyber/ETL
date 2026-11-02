package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import java.io.File;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;

public class NetSuiteWsdlFile {
   private static final NetSuiteWsdlFile INSTANCE = new NetSuiteWsdlFile();
   private Throwable e;
   private File netsuiteWsdlCacheDir;
   private File netsuiteConfig;

   public static File getNetSuiteConfigFile() {
      return getInstance().netsuiteConfig;
   }

   public static File getNetSuiteWsdlCacheFolder() {
      return getInstance().netsuiteWsdlCacheDir;
   }

   private static NetSuiteWsdlFile getInstance() {
      if (INSTANCE.e != null) {
         throw new ConnectorServerRuntimeException(INSTANCE.e);
      } else {
         return INSTANCE;
      }
   }

   private NetSuiteWsdlFile() {
      try {
         File var1 = ConnectorExecutionSystem.getDataInterchangeDir();
         File var2 = new File(var1, "xml_dtd/Schemas/Connectors");
         this.netsuiteWsdlCacheDir = new File(var1, "Temp/cache/netsuite");
         this.netsuiteConfig = new File(var2, "netsuiteconfig.xml");
         this.e = null;
      } catch (Throwable var3) {
         var3.printStackTrace();
         this.e = var3;
      }
   }
}
