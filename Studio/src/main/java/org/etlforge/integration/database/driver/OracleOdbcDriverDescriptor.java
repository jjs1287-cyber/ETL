package org.etlforge.integration.database.driver;

import org.etlforge.integration.database.BeginEndQuote;

public final class OracleOdbcDriverDescriptor extends OdbcDriverDescriptor {
   public OracleOdbcDriverDescriptor(String var1, BeginEndQuote var2) {
      super(var1, var2);
   }

   public OracleOdbcDriverDescriptor(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   @Override
   protected String constructStandardConnectionString(ConnectionParams var1) {
      return "DRIVER={"
         + this.getName()
         + "};SERVER="
         + var1.getServer()
         + ";UID="
         + var1.getUser()
         + (var1.getPassword() == null ? "" : ";PWD=" + var1.getPassword())
         + ";";
   }

   @Override
   public void checkConnectionParams(ConnectionParams var1) throws InvalidConnectionParamsException {
      String var2 = var1.getServer();
      if (var2 == null || var2.trim().length() == 0) {
         throw new InvalidConnectionParamsException(var1, "Must specify a server name.");
      }
   }

   @Override
   public boolean isProbablyPostgreSQL() {
      return false;
   }

   @Override
   public boolean isProbablyOracle() {
      return true;
   }
}
