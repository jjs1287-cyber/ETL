package org.etlforge.integration.data.structure.database;

import org.etlforge.util.persist.Persistor;

public class SqlScriptPersistor {
   private static final String ENTRY = "Entry";
   private static final String ID = "entryId";
   private static final String TYPE = "type";
   private static final String SCRIPT = "script";
   private static final String WHERE = "where";

   public void save(SqlScript var1, Persistor var2) {
      for (SqlScript.Entry var6 : var1.getAllEntries()) {
         this.saveEntry(var6, var2.createChild("Entry"));
      }
   }

   public SqlScript restoreFrom(Persistor var1) {
      SqlScript var2 = new SqlScript();

      for (Persistor var4 : var1.getChildren("Entry")) {
         var2.addEntry(this.restoreEntry(var4));
      }

      return var2;
   }

   private void saveEntry(SqlScript.Entry var1, Persistor var2) {
      var2.putLong("entryId", var1.getId());
      var2.putInteger("type", var1.getType());
      var2.putText("script", var1.getScript());
      if (var1.getWhereClause() != null) {
         var2.putText("where", var1.getWhereClause());
      }
   }

   private SqlScript.Entry restoreEntry(Persistor var1) {
      return new SqlScript.Entry(var1.getLong("entryId"), var1.getInteger("type"), var1.getText("script"), var1.getText("where"));
   }
}
