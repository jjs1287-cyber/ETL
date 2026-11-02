package org.etlforge.integration.server.engine.connector.netsuite.definition;

import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.wsdl.NetSuiteDefinitionContentsFactory;
import org.etlforge.integration.server.netsuite.config.NetSuiteConfigFactory;

public class NetSuiteConfigInstance {
   private static final NetSuiteConfigInstance INSTANCE = new NetSuiteConfigInstance();
   private Throwable e = null;
   private final NetSuiteConfig config = this.build();

   public static NetSuiteConfig getNetSuiteConfig() {
      return INSTANCE.getNetSuiteConfigImpl();
   }

   private NetSuiteConfig getNetSuiteConfigImpl() {
      if (this.e != null) {
         throw new ConnectorServerRuntimeException(this.e);
      } else {
         return this.config;
      }
   }

   NetSuiteConfigInstance() {
   }

   private NetSuiteConfig build() {
      try {
         String var1 = NetSuiteDefinitionContentsFactory.getConfigXml();
         return NetSuiteConfigFactory.create(var1);
      } catch (Throwable var3) {
         var3.printStackTrace();
         this.e = var3;
         return null;
      }
   }
}
