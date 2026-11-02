package org.etlforge.integration.server.implementation.webservice.interchange.db.impl;

import com.google.common.base.Function;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.integration.database.info.DatabaseView;
import org.etlforge.integration.database.info.SystemTable;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsDatabaseColumn;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsDatabaseObject;
import org.etlforge.lang.Arrays2;

final class OdbcFunctions {
   public static final Function<DatabaseObject, WsDatabaseObject> TO_WS_TABLES = new Function<DatabaseObject, WsDatabaseObject>() {
      public WsDatabaseObject apply(DatabaseObject var1) {
         WsDatabaseObject var2 = new WsDatabaseObject();
         var2.setSchemaName(var1.getSchema());
         var2.setObjectName(var1.getName());
         return var2;
      }
   };
   public static final Function<WsDatabaseObject, DatabaseObject> FROM_WS_OBJECT = new Function<WsDatabaseObject, DatabaseObject>() {
      public DatabaseObject apply(WsDatabaseObject var1) {
         if (var1 == null) {
            return null;
         }

         try {
            DatabaseObjectType var2 = DatabaseObjectType.fromString(var1.getObjectType());
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
               case SYSTEM_TABLE:
                  var5 = new SystemTable(var4, var3);
            }

            if (var5 != null) {
               for (DatabaseColumn var9 : Arrays2.transform(var1.getColumns(), DatabaseColumn.class, OdbcFunctions.FROM_WS_COLUMN)) {
                  var5.addColumn(var9);
               }
            }

            return var5;
         } catch (Exception var10) {
            ErrorLog.log(JdbcInfoProviderWsImpl.class, "Unexpected data returned from the server: " + var10.getMessage(), var10);
            return null;
         }
      }
   };
   public static final Function<WsDatabaseColumn, DatabaseColumn> FROM_WS_COLUMN = new Function<WsDatabaseColumn, DatabaseColumn>() {
      public DatabaseColumn apply(WsDatabaseColumn var1) {
         return new DatabaseColumn(
            null,
            var1.getName(),
            var1.getType(),
            var1.getSqlType(),
            var1.getDefaultValue(),
            var1.getIsNullable(),
            var1.getIsPrimaryKey(),
            var1.getCharacterSize(),
            var1.getDecimalDigits()
         );
      }
   };

   private OdbcFunctions() {
   }
}
