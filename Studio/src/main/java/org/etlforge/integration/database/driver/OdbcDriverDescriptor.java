package org.etlforge.integration.database.driver;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.BeginEndQuote;

public class OdbcDriverDescriptor extends DriverDescriptor {
   public OdbcDriverDescriptor(String var1, BeginEndQuote var2) {
      super(var1, DriverType.ODBC, var2);
   }

   public OdbcDriverDescriptor(String var1, String var2, String var3) {
      super(var1, DriverType.ODBC, new BeginEndQuote(var2, var3));
   }

   @Override
   public final String getConnectionString(ConnectionParams var1) {
      String var2 = var1.getManualConnectionString();
      return StringUtils.isNotEmpty(var2) ? this.constructManualConnectionString(var1, var2) : this.constructConnectionString(var1);
   }

   private String constructManualConnectionString(ConnectionParams var1, String var2) {
      if (StringUtils.isNotEmpty(var1.getPassword()) && !containsPassword(var2)) {
         if (!var2.endsWith(";")) {
            var2 = var2 + ";";
         }

         var2 = var2 + "PWD=" + var1.getPassword() + ";";
      }

      return var2;
   }

   private static boolean containsPassword(String var0) {
      String var1 = var0.toLowerCase();
      return var1.contains(";pwd=") || var1.contains(";password=");
   }

   private String constructConnectionString(ConnectionParams var1) {
      String var2 = this.constructStandardConnectionString(var1);
      String var3 = var1.getAdditionalParams();
      if (StringUtils.isEmpty(var3)) {
         return var2;
      }

      String var4 = var2;
      if (!var4.endsWith(";")) {
         var4 = var4 + ";";
      }

      return var4 + var3;
   }

   protected String constructStandardConnectionString(ConnectionParams var1) {
      int var2 = var1.getPort().getPort();
      String var3 = var1.getPassword();
      return "DRIVER={"
         + this.getName()
         + "};SERVER="
         + var1.getServer()
         + ";DATABASE="
         + var1.getDatabase()
         + ";UID="
         + var1.getUser()
         + (var2 <= 0 ? "" : ";PORT=" + String.valueOf(var2))
         + ";OPTION=3"
         + (var3 == null ? "" : ";PWD=" + var3);
   }

   @Override
   public final void accept(DriverDescriptorVisitor var1) {
      var1.visit(this);
   }

   @Override
   public boolean isProbablyPostgreSQL() {
      return this.getName().toLowerCase().contains("postgres");
   }

   @Override
   public boolean isProbablyOracle() {
      return false;
   }

   @Override
   public final boolean isManualSqlStatementSupported() {
      return false;
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof OdbcDriverDescriptor ? super.equalsDescriptor((DriverDescriptor)var1) : false;
      }
   }

   @Override
   public final int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.getName().hashCode();
      var1 = 31 * var1 + this.getType().hashCode();
      BeginEndQuote var2 = this.getBeginEndQuotes();
      return 31 * var1 + (var2 != null ? var2.hashCode() : 0);
   }
}
