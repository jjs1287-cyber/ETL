package org.etlforge.integration.server.implementation.webservice.interchange.db;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.SystemUtils;

final class TableInfoLogger {
   private final Logger log;
   private StringBuilder tableString;
   private DatabaseObject table;
   private BeginEndQuote quotes;

   public TableInfoLogger(Logger var1) {
      this.log = var1;
   }

   public void log(DatabaseObject[] var1, BeginEndQuote var2) {
      if (this.log.isLoggable(Level.INFO)) {
         String var3 = this.createLogString(var1, var2);
         this.log.info(var3);
      }
   }

   private String createLogString(DatabaseObject[] var1, BeginEndQuote var2) {
      this.quotes = var2;
      this.tableString = new StringBuilder("Populating the following tables:");
      this.newLine();
      this.tableString.append("(Begin-End Quote: ").append(var2).append(")");
      this.newLine();

      for (DatabaseObject var6 : var1) {
         this.logTableInfo(var6);
      }

      return this.tableString.toString();
   }

   private void logTableInfo(DatabaseObject var1) {
      this.table = var1;
      this.appendSchemaAndTableName();
      if (this.shouldColumnsBeLogged()) {
         this.appendColumns();
      }
   }

   private void appendSchemaAndTableName() {
      this.tableString.append("Schema: ").append(this.table.getSchema());
      this.tableString.append("  Name: ").append(this.table.getName());
      if (this.quotes != null) {
         this.tableString.append("  Simple Name: ").append(this.quotes.getSimpleName(this.table));
      }

      this.newLine();
   }

   private boolean shouldColumnsBeLogged() {
      return this.log.isLoggable(Level.FINE);
   }

   private void appendColumns() {
      this.tableString.append("Columns:");
      this.newLine();

      for (DatabaseColumn var2 : this.table.getAllColumns()) {
         this.tableString
            .append(var2.getName())
            .append(" [")
            .append(var2.getSqlDataType())
            .append("]")
            .append(" Default value: ")
            .append(var2.getDefaultValueString());
         this.newLine();
      }
   }

   private void newLine() {
      this.tableString.append(SystemUtils.LINE_SEPARATOR);
   }
}
