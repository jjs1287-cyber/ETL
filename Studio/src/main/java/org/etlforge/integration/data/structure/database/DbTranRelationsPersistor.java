package org.etlforge.integration.data.structure.database;

import org.etlforge.util.persist.Persistor;

public class DbTranRelationsPersistor {
   private static final String RELATION = "Relation";
   private static final String ID = "relationId";
   private static final String PARENT_TABLE = "parentTable";
   private static final String PARENT_FIELD = "parentField";
   private static final String CHILD_TABLE = "childTable";
   private static final String CHILD_FIELD = "childField";
   private static final String OUTER_JOIN = "outerJoin";
   private static final String ONE_TO_ONE = "onetoOne";
   private static final String IS_SOURCE = "isSource";

   public void save(DbTranRelations var1, Persistor var2) {
      for (DbTranRelations.Relation var4 : var1.getRelations()) {
         this.saveRelation(var4, var2.createChild("Relation"));
      }
   }

   private void saveRelation(DbTranRelations.Relation var1, Persistor var2) {
      var2.putLong("relationId", var1.getId());
      var2.putString("parentTable", var1.getParentName());
      var2.putString("parentField", var1.getParentField());
      var2.putString("childTable", var1.getChildName());
      var2.putString("childField", var1.getChildField());
      var2.putBoolean("outerJoin", var1.isOuterJoin());
      var2.putBoolean("onetoOne", var1.isOneToOne());
      var2.putBoolean("isSource", var1.isSource());
   }

   public DbTranRelations restoreFrom(Persistor var1) {
      DbTranRelations var2 = new DbTranRelations();

      for (Persistor var4 : var1.getChildren("Relation")) {
         var2.addRelation(this.restoreRelation(var4));
      }

      return var2;
   }

   private DbTranRelations.Relation restoreRelation(Persistor var1) {
      DbTranRelations.Relation var2 = new DbTranRelations.Relation(
         var1.getLong("relationId"),
         var1.getString("parentTable"),
         var1.getString("parentField"),
         var1.getString("childTable"),
         var1.getString("childField"),
         var1.getBoolean("outerJoin"),
         var1.getBoolean("onetoOne")
      );
      var2.setIsSource(var1.getBoolean("isSource"));
      return var2;
   }
}
