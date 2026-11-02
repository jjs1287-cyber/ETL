package org.etlforge.integration.server.implementation.webservice.interchange.db;

import com.google.common.base.Function;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.integration.database.info.DatabaseView;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.integration.dataelement.DataElementContext;
import org.etlforge.integration.dataelement.DataElementResolver;

class DatabaseObjectConverter implements Function<WsJdbcDatabaseObject, DatabaseObject> {
   private final DataElementResolver deResolver = new DataElementResolver();

   public DatabaseObjectConverter(DataElementContext var1) {
      this.deResolver.setContext(var1);
   }

   public DatabaseObject apply(WsJdbcDatabaseObject var1) {
      String var2 = var1.getSchemaName();
      String var3 = var1.getObjectName();
      DatabaseObjectType var4 = DatabaseObjectType.valueOf(var1.getObjectType());
      DatabaseObject var5;
      switch (var4) {
         case TABLE:
            var5 = new DatabaseTable(var3, var2);
            break;
         case VIEW:
            var5 = new DatabaseView(var3, var2);
            break;
         case MANUAL_SQL:
            String var6 = var3;
            var5 = new ManualSqlStatement(var6);
            break;
         default:
            throw new RuntimeException("Unexpected type of database object: " + var4);
      }

      return var5;
   }
}
