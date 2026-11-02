package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteWsdlContentsCache {
   private static NetSuiteWsdlContentsCacheLocker lockers = new NetSuiteWsdlContentsCacheLocker();

   public static NetSuiteWsdlContents getNetSuiteWsdlContents(ProxySettings var0, String var1) {
      synchronized (lockers.getLocker(var1)) {
         return getNetSuiteWsdlContentsImpl(var0, var1);
      }
   }

   private static NetSuiteWsdlContents getNetSuiteWsdlContentsImpl(ProxySettings var0, String var1) {
      NetSuiteWsdlContentsFactory var2 = new NetSuiteWsdlContentsFactory(var1);
      return var2.create(var0);
   }
}
