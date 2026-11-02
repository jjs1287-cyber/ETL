package org.etlforge.integration.database.info;

public class DatabaseSynonym extends DatabaseObject {
   public DatabaseSynonym(String var1, String var2) {
      super(var1, var2);
   }

   public DatabaseSynonym(DatabaseSynonym var1, String var2) {
      super(var1, var2);
   }

   @Override
   public DatabaseObjectType getType() {
      return DatabaseObjectType.SYNONYM;
   }
}
