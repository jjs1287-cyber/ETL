package org.etlforge.integration.server.engine.connector.netsuite.adapter;

import com.google.common.base.Strings;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuiteAuthetication;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;

public class ConnectorEndpointNetSuiteAdapter {
   private final NetSuitePassport passport;

   public ConnectorEndpointNetSuiteAdapter(ConnectorEndpointParameters var1) {
      this.passport = convert(var1);
   }

   public boolean testConnection() {
      this.testConnectionImpl();
      return true;
   }

   public NetSuitePassport getNetSuitePassport() {
      return this.passport;
   }

   private synchronized void testConnectionImpl() {
      this.login();
      this.logout();
   }

   private void login() {
      NetSuiteAuthetication var1 = new NetSuiteAuthetication(this.passport);
      var1.login();
   }

   private void logout() {
      NetSuiteAuthetication var1 = new NetSuiteAuthetication(this.passport);
      var1.logout();
   }

   public static NetSuitePassport convert(ConnectorEndpointParameters var0) {
      Properties var1 = var0.getParameters();
      String var2 = var1.getProperty("email");
      String var3 = var1.getProperty("sandbox");
      String var4 = var1.getProperty("wsdldownloadurl");
      boolean var5 = false;
      if (!Strings.isNullOrEmpty(var3)) {
         var5 = var3.equals("1") || var3.equalsIgnoreCase("true");
      }

      NetSuitePassport var6 = new NetSuitePassport(var0.getProxySettings(), var5, var2, var0.getPassword(), var0.getUser(), var4);
      var6.validate();
      return var6;
   }
}
