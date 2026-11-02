package org.etlforge.integration.database.driver;

import org.etlforge.integration.database.BeginEndQuote;

public final class JdbcDriverDesriptorBuilder {
   private static final String ORACLE_MARKER = "ora";
   private static final String SQL_SERVER_MARKER = "sqlserver";
   private String name;
   private String subprotocol = "postgresql";
   private String driverClass;
   private BeginEndQuote quotes;

   public JdbcDriverDesriptorBuilder name(String var1) {
      this.name = var1;
      return this;
   }

   public JdbcDriverDesriptorBuilder subprotocol(String var1) {
      if (var1 == null) {
         var1 = "postgresql";
      }

      this.subprotocol = var1;
      return this;
   }

   public JdbcDriverDesriptorBuilder driverClass(String var1) {
      this.driverClass = var1;
      return this;
   }

   public JdbcDriverDesriptorBuilder quotes(BeginEndQuote var1) {
      this.quotes = var1;
      return this;
   }

   public JdbcDriverDescriptor build() {
      if (this.subprotocol.contains("sqlserver")) {
         return new SQLServerJdbcDriverDescriptor(this.name, this.subprotocol, this.driverClass, this.quotes);
      } else {
         return this.subprotocol.contains("ora")
            ? new OracleJdbcDriverDescriptor(this.name, this.subprotocol, this.driverClass, this.quotes)
            : new JdbcDriverDescriptor(this.name, this.subprotocol, this.driverClass, this.quotes);
      }
   }

   public void reset() {
      this.name = null;
      this.subprotocol = "postgresql";
      this.driverClass = null;
      this.quotes = null;
   }
}
