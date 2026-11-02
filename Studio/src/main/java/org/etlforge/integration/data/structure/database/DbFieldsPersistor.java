package org.etlforge.integration.data.structure.database;

import org.etlforge.util.persist.Persistor;

public class DbFieldsPersistor {
   private static final String ENTRY = "Entry";
   private static final String ID = "entryId";
   private static final String TYPE = "type";
   private static final String KEYFIELDS = "keyFields";

   public void save(DbFields var1, Persistor var2) {
      for (DbFields.Entry var6 : var1.getAllEntries()) {
         this.saveEntry(var6, var2.createChild("Entry"));
      }
   }

   public DbFields restoreFrom(Persistor var1) {
      DbFields var2 = new DbFields();

      for (Persistor var4 : var1.getChildren("Entry")) {
         var2.addEntry(this.restoreEntry(var4));
      }

      return var2;
   }

   private void saveEntry(DbFields.Entry var1, Persistor var2) {
      var2.putLong("entryId", var1.getId());
      var2.putInteger("type", var1.getType());
      var2.putText("keyFields", var1.getKeyFields());
   }

   private DbFields.Entry restoreEntry(Persistor var1) {
      return new DbFields.Entry(var1.getLong("entryId"), var1.getText("keyFields"), var1.getInteger("type"));
   }
}
