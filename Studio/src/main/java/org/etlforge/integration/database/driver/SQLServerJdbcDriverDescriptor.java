package org.etlforge.integration.database.driver;

import org.etlforge.integration.database.BeginEndQuote;

final class SQLServerJdbcDriverDescriptor extends JdbcDriverDescriptor {
   public SQLServerJdbcDriverDescriptor(String var1, String var2, String var3, BeginEndQuote var4) {
      super(var1, var2, var3, var4);
   }

   @Override
   protected void buildConnectionStringFromParts(ConnectionParams var1, StringBuilder var2) {
      String var3 = var1.getServer();
      if (var3.length() > 0) {
         var2.append("//").append(var3);
      }

      int var4 = var1.getPort().getPort();
      if (var4 > 0) {
         var2.append(":").append(var4);
      }

      String var5 = var1.getDatabase();
      if (var5.length() > 0) {
         var2.append(";DatabaseName=").append(var5);
      }
   }

   @Override
   public boolean isProbablyOracle() {
      return false;
   }
}
