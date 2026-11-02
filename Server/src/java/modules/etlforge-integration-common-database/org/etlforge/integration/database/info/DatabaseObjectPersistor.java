package org.etlforge.integration.database.info;

import org.etlforge.util.persist.Persistor;

public final class DatabaseObjectPersistor {
   private static final String TYPE = "type";
   private static final String NAME = "name";
   private static final String SCHEMA = "schema";
   private static final String COLUMN = "Column";

   public void save(DatabaseObject var1, Persistor var2) {
      var2.putString("type", this.getTypeFrom(var1));
      var2.putString("name", var1.getName());
      var2.putString("schema", var1.getSchema());
      this.saveColumns(var1, var2);
   }

   public DatabaseObject restoreFrom(Persistor var1) {
      DatabaseObject var2 = this.restoreEmptyObject(var1);
      this.restoreColumns(var2, var1);
      return var2;
   }

   private void saveColumns(DatabaseObject var1, Persistor var2) {
      DatabaseColumnPersistor var3 = new DatabaseColumnPersistor();

      for (DatabaseColumn var5 : var1.getAllColumns()) {
         var3.save(var5, var2.createChild("Column"));
      }
   }

   private void restoreColumns(DatabaseObject var1, Persistor var2) {
      DatabaseColumnPersistor var3 = new DatabaseColumnPersistor();

      for (Persistor var5 : var2.getChildren("Column")) {
         var1.addColumn(var3.restoreFrom(var5, var1));
      }
   }

   private String getTypeFrom(DatabaseObject var1) {
      return var1.getType().name().toLowerCase();
   }

   private DatabaseObject restoreEmptyObject(Persistor var1) {
      String var2 = var1.getString("name");
      String var3 = var1.getString("schema");
      String var4 = var1.getString("type");
      if (isType(var4, DatabaseObjectType.TABLE) || isType(var4, DatabaseObjectType.SYSTEM_TABLE)) {
         return new DatabaseTable(var2, var3);
      } else if (isType(var4, DatabaseObjectType.VIEW)) {
         return new DatabaseView(var2, var3);
      } else if (isType(var4, DatabaseObjectType.SYNONYM)) {
         return new DatabaseSynonym(var2, var3);
      } else if (isType(var4, DatabaseObjectType.MANUAL_SQL)) {
         return new ManualSqlStatement(var2);
      } else {
         throw new IllegalArgumentException("Invalid persistor: the type field is missing or invalid (" + var4 + ")");
      }
   }

   private static boolean isType(String var0, DatabaseObjectType var1) {
      return var1.name().toLowerCase().equals(var0);
   }
}
