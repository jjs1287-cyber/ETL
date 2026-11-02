package org.etlforge.integration.database.driver;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.BeginEndQuote;

final class OracleJdbcDriverDescriptor extends JdbcDriverDescriptor {
   public OracleJdbcDriverDescriptor(String var1, String var2, String var3, BeginEndQuote var4) {
      super(var1, var2, var3, var4);
   }

   @Override
   public void checkConnectionParams(ConnectionParams var1) throws InvalidConnectionParamsException {
      if (StringUtils.isBlank(var1.getServer())) {
         throw new InvalidConnectionParamsException(var1, "Must specify a server.");
      }
   }

   @Override
   protected void buildConnectionStringFromParts(ConnectionParams var1, StringBuilder var2) {
      String var3 = var1.getServer();
      int var4 = var1.getPort().getPort();
      if (var3.length() > 0) {
         var2.append("@").append(var3);
      }

      if (var4 > 0) {
         var2.append(":").append(var4);
      }
   }

   @Override
   public boolean isProbablyOracle() {
      return true;
   }
}
