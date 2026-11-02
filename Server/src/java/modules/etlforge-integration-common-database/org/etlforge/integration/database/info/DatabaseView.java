package org.etlforge.integration.database.info;

public class DatabaseView extends DatabaseObject {
   public DatabaseView(String var1, String var2) {
      super(var1, var2);
   }

   public DatabaseView(DatabaseView var1, String var2) {
      super(var1, var2);
   }

   @Override
   public DatabaseObjectType getType() {
      return DatabaseObjectType.VIEW;
   }
}
