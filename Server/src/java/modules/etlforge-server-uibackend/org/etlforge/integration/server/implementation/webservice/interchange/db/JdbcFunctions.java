package org.etlforge.integration.server.implementation.webservice.interchange.db;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

final class JdbcFunctions {
   public static final Function<DatabaseObject, WsJdbcDatabaseObject> TO_WS_OBJECT = new Function<DatabaseObject, WsJdbcDatabaseObject>() {
      public WsJdbcDatabaseObject apply(DatabaseObject var1) {
         WsJdbcDatabaseObject var2 = new WsJdbcDatabaseObject();
         var2.setSchemaName(var1.getSchema());
         var2.setObjectName(var1.getName());
         ArrayList var3 = Lists.newArrayList();

         for (DatabaseColumn var5 : var1.getAllColumns()) {
            WsJdbcDatabaseColumn var6 = new WsJdbcDatabaseColumn(
               var5.getName(),
               var5.getDataTypeString(),
               ColumnDefaultValueGuard.guard(var5.getDefaultValueString()),
               var5.getSqlDataType(),
               var5.isPrimaryKey(),
               var5.isNullable(),
               var5.getColumnSize(),
               var5.getDecimalDigits()
            );
            var3.add(var6);
         }

         var2.setColumns((WsJdbcDatabaseColumn[]) var3.toArray(new WsJdbcDatabaseColumn[var3.size()]));
         var2.setObjectType(var1.getType().name());
         return var2;
      }
   };
   public static final Function<JdbcDriverDescriptor, WsJdbcDriverDescriptor> TO_WS_DRIVER = new Function<JdbcDriverDescriptor, WsJdbcDriverDescriptor>() {
      public WsJdbcDriverDescriptor apply(JdbcDriverDescriptor var1) {
         WsJdbcDriverDescriptor var2 = new WsJdbcDriverDescriptor();
         var2.setName(var1.getName());
         var2.setSubprotocol(var1.getSubprotocol());
         var2.setDriverClass(var1.getDriverClass());
         BeginEndQuote var3 = var1.getBeginEndQuotes();
         WsJdbcBeginEndQuote var4 = null;
         if (var3 != null) {
            var4 = new WsJdbcBeginEndQuote(var3.begin, var3.end);
         }

         var2.setBeginEndQuote(var4);
         return var2;
      }
   };

   private JdbcFunctions() {
   }
}
