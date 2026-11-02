package org.etlforge.integration.database.info;

public final class SystemTable extends DatabaseObject {
   public SystemTable(DatabaseObject var1, String var2) {
      super(var1, var2);
   }

   public SystemTable(String var1, String var2) {
      super(var1, var2);
   }

   @Override
   public DatabaseObjectType getType() {
      return DatabaseObjectType.SYSTEM_TABLE;
   }
}
