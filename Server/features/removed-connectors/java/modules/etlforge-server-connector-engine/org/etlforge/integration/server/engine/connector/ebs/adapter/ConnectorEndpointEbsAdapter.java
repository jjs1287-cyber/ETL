package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.ubs.core.adapter.ebs.connection.EbsExecutionConnection;
import java.util.Properties;

public class ConnectorEndpointEbsAdapter {
   static final String ORACLEDRIVER = "oracle.jdbc.driver.OracleDriver";
   static final String JDBCPREFIX = "jdbc:";
   static final String CONNECTIONURLPREFIX = "jdbc:oracle:thin:@";
   public static final String CONNECTIONURLNAME = "connecturl";
   private final String connectionurl;
   private final String userName;
   private final String password;

   public ConnectorEndpointEbsAdapter(String var1, String var2, String var3) {
      this.connectionurl = getConnectionUrl(var1);
      this.userName = var2;
      this.password = var3;
   }

   public boolean testConnection() {
      Properties var1 = this.getEbsConnectionProperties();
      EbsExecutionConnection var2 = EbsConnectionWrapper.getOpenExecutionConnection(var1);
      EbsConnectionWrapper.close(var2);
      return true;
   }

   private static void putProperties(Properties var0, String var1, String var2) {
      var0.put(var1, var2 == null ? "" : var2);
   }

   Properties getEbsConnectionProperties() {
      Properties var1 = new Properties();
      putProperties(var1, "username", this.userName);
      putProperties(var1, "password", this.password);
      putProperties(var1, "drivername", "oracle.jdbc.driver.OracleDriver");
      putProperties(var1, "connecturl", this.connectionurl);
      return var1;
   }

   private static String getConnectionUrl(String var0) {
      if (var0.length() < "jdbc:".length()) {
         return "jdbc:oracle:thin:@" + var0;
      }

      String var1 = var0.substring(0, "jdbc:".length());
      return "jdbc:".equalsIgnoreCase(var1) ? var0 : "jdbc:oracle:thin:@" + var0;
   }
}
