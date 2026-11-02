package org.etlforge.integration.database.info;

public class DatabaseTable extends DatabaseObject {
   public DatabaseTable(String var1, String var2) {
      super(var1, var2);
   }

   public DatabaseTable(DatabaseTable var1, String var2) {
      super(var1, var2);
   }

   @Override
   public DatabaseObjectType getType() {
      return DatabaseObjectType.TABLE;
   }
}
