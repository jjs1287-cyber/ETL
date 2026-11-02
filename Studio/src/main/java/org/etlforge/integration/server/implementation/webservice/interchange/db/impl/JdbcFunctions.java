package org.etlforge.integration.server.implementation.webservice.interchange.db.impl;

import com.google.common.base.Function;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.JdbcDriverDesriptorBuilder;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.integration.database.info.DatabaseView;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcBeginEndQuote;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcDatabaseColumn;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcDatabaseObject;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcDriverDescriptor;
import org.etlforge.lang.Arrays2;

final class JdbcFunctions {
   public static final Function<DatabaseObject, WsJdbcDatabaseObject> TO_WS_TABLES = new Function<DatabaseObject, WsJdbcDatabaseObject>() {
      public WsJdbcDatabaseObject apply(DatabaseObject var1) {
         String var2 = var1.getSchema();
         String var3 = var1.getName();
         DatabaseObjectType var4 = var1.getType();
         return new WsJdbcDatabaseObject(var4.name(), var3, var2, new WsJdbcDatabaseColumn[0]);
      }
   };
   public static final Function<WsJdbcDatabaseObject, DatabaseObject> FROM_WS_OBJECT = new Function<WsJdbcDatabaseObject, DatabaseObject>() {
      public DatabaseObject apply(WsJdbcDatabaseObject var1) {
         try {
            DatabaseObjectType var2 = DatabaseObjectType.valueOf(var1.getObjectType());
            String var3 = var1.getSchemaName();
            String var4 = var1.getObjectName();
            DatabaseObject var5 = null;
            switch (var2) {
               case TABLE:
                  var5 = new DatabaseTable(var4, var3);
                  break;
               case VIEW:
                  var5 = new DatabaseView(var4, var3);
                  break;
               case MANUAL_SQL:
                  var5 = new ManualSqlStatement(var4);
                  break;
               default:
                  throw new RuntimeException("Unexpected object type: " + var2);
            }

            for (DatabaseColumn var9 : Arrays2.transform(var1.getColumns(), DatabaseColumn.class, JdbcFunctions.FROM_WS_COLUMN)) {
               var5.addColumn(var9);
            }

            return var5;
         } catch (Exception var10) {
            ErrorLog.log(JdbcInfoProviderWsImpl.class, "Unexpected data returned from the server: " + var10.getMessage(), var10);
            return null;
         }
      }
   };
   public static final Function<WsJdbcDatabaseColumn, DatabaseColumn> FROM_WS_COLUMN = new Function<WsJdbcDatabaseColumn, DatabaseColumn>() {
      public DatabaseColumn apply(WsJdbcDatabaseColumn var1) {
         return new DatabaseColumn(
            null,
            var1.getName(),
            var1.getType(),
            var1.getSqlType(),
            var1.getDefaultValue(),
            var1.getIsNullable(),
            var1.getIsPrimaryKey(),
            var1.getColumnSize(),
            var1.getDecimalDigits()
         );
      }
   };
   public static final Function<WsJdbcDriverDescriptor, JdbcDriverDescriptor> FROM_WS_DRIVER = new Function<WsJdbcDriverDescriptor, JdbcDriverDescriptor>() {
      public JdbcDriverDescriptor apply(WsJdbcDriverDescriptor var1) {
         JdbcDriverDesriptorBuilder var2 = new JdbcDriverDesriptorBuilder();
         BeginEndQuote var3 = JdbcFunctions.FROM_WS_QUOTES.apply(var1.getBeginEndQuote());
         var2.name(var1.getName()).driverClass(var1.getDriverClass()).subprotocol(var1.getSubprotocol()).quotes(var3);
         return var2.build();
      }
   };
   public static final Function<WsJdbcBeginEndQuote, BeginEndQuote> FROM_WS_QUOTES = new Function<WsJdbcBeginEndQuote, BeginEndQuote>() {
      public BeginEndQuote apply(WsJdbcBeginEndQuote var1) {
         return var1 != null ? new BeginEndQuote(var1.getBeginQuote(), var1.getEndQuote()) : null;
      }
   };

   private JdbcFunctions() {
   }
}
