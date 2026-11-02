package org.etlforge.integration.database.driver;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.BeginEndQuote;

public class JdbcDriverDescriptor extends DriverDescriptor {
   private final String driverClass;
   private final String subprotocol;

   public JdbcDriverDescriptor(String var1, String var2, String var3, BeginEndQuote var4) {
      super(var1, DriverType.JDBC, var4);
      Preconditions.checkArgument(StringUtils.isNotBlank(var3), "Must provide a driver class");
      Preconditions.checkArgument(StringUtils.isNotBlank(var2), "Must provide a subprotocol");
      this.driverClass = var3;
      this.subprotocol = var2;
   }

   @Override
   public final String getConnectionString(ConnectionParams var1) {
      String var2 = var1.getManualConnectionString();
      if (StringUtils.isNotEmpty(var2)) {
         return var2;
      }

      StringBuilder var3 = new StringBuilder("jdbc:").append(this.getSubprotocol()).append(":");
      this.buildConnectionStringFromParts(var1, var3);
      this.addAdditionalConnectionParamsToUrl(var1, var3);
      return var3.toString();
   }

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
         var2.append("/").append(var5);
      }
   }

   private void addAdditionalConnectionParamsToUrl(ConnectionParams var1, StringBuilder var2) {
      String var3 = var1.getAdditionalParams();
      if (StringUtils.isNotBlank(var3)) {
         String var4 = this.getParameterStart();
         if (!var3.startsWith(var4)) {
            var2.append(var4);
         }

         var2.append(var3);
      }
   }

   private String getParameterStart() {
      return !this.isPostgres() && !this.isMySQL() ? ";" : "?";
   }

   private boolean isMySQL() {
      return this.subprotocol.equalsIgnoreCase("mysql");
   }

   private boolean isPostgres() {
      return this.subprotocol.equals("postgresql");
   }

   public final String getDriverClass() {
      return this.driverClass;
   }

   public final String getSubprotocol() {
      return this.subprotocol;
   }

   @Override
   public final boolean isManualSqlStatementSupported() {
      return true;
   }

   @Override
   public final void accept(DriverDescriptorVisitor var1) {
      var1.visit(this);
   }

   public final void loadDriverClass() throws ClassNotFoundException {
      if (!StringUtils.isEmpty(this.driverClass)) {
         Class.forName(this.driverClass);
      }
   }

   @Override
   public String toString() {
      return String.format("%s - %s - %s", this.subprotocol, this.driverClass, super.toString());
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof JdbcDriverDescriptor)) {
         return false;
      }

      JdbcDriverDescriptor var2 = (JdbcDriverDescriptor)var1;
      return super.equalsDescriptor(var2) && this.driverClass.equals(var2.driverClass) && this.subprotocol.equals(var2.subprotocol);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.getName().hashCode();
      var1 = 31 * var1 + this.getType().hashCode();
      BeginEndQuote var2 = this.getBeginEndQuotes();
      var1 = 31 * var1 + (var2 != null ? var2.hashCode() : 0);
      var1 = 31 * var1 + this.driverClass.hashCode();
      return 31 * var1 + this.subprotocol.hashCode();
   }

   @Override
   public boolean isProbablyOracle() {
      return this.isProbablyDatabaseType("ora");
   }

   private boolean isProbablyDatabaseType(String var1) {
      return this.driverClass.toLowerCase().contains(var1) || this.subprotocol.toLowerCase().contains(var1);
   }

   @Override
   public boolean isProbablyPostgreSQL() {
      return this.isProbablyDatabaseType("postgres");
   }
}
