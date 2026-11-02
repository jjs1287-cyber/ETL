package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbTranRelations;

final class TableRelationshipDeployFieldBuilder {
   private static final String SEPARATOR = ",";
   private static final String SOURCE = "S";
   private static final String TARGET = "T";
   private static final String TRUE = "1";
   private static final String FALSE = "0";
   private final Map<TableRelationshipDeployFieldBuilder.MapKey, TableRelationshipDeployFieldBuilder.JoinedTables> joins = Maps.newLinkedHashMap();

   public TableRelationshipDeployFieldBuilder() {
   }

   public List<String[]> build(DatabaseStructure var1, String var2) {
      DbTranRelations var3 = var1.getRelations();

      for (DbTranRelations.Relation var5 : var3.getRelations()) {
         TableRelationshipDeployFieldBuilder.MapKey var6 = new TableRelationshipDeployFieldBuilder.MapKey(var5);
         TableRelationshipDeployFieldBuilder.JoinedTables var7 = null;
         if (this.joins.containsKey(var6)) {
            var7 = this.joins.get(var6);
         } else {
            this.joins.put(var6, var7 = new TableRelationshipDeployFieldBuilder.JoinedTables(var5));
         }

         var7.addFieldRelation(var5);
      }

      return this.getColumns(var2);
   }

   private List<String[]> getColumns(String var1) {
      ArrayList var2 = Lists.newArrayList();

      for (TableRelationshipDeployFieldBuilder.JoinedTables var4 : this.joins.values()) {
         var2.add(var4.getDeployColumns(var1));
      }

      return var2;
   }

   private static class JoinedTables {
      private final String parentTable;
      private final String childTable;
      private final boolean isSource;
      private final boolean isOuterJoin;
      private final boolean isOneToOne;
      private final StringBuilder parentFields;
      private final StringBuilder childFields;

      public JoinedTables(DbTranRelations.Relation var1) {
         this.parentTable = var1.getParentName();
         this.childTable = var1.getChildName();
         this.isSource = var1.isSource();
         this.isOuterJoin = var1.isOuterJoin();
         this.isOneToOne = var1.isOneToOne();
         this.parentFields = new StringBuilder();
         this.childFields = new StringBuilder();
      }

      public void addFieldRelation(DbTranRelations.Relation var1) {
         if (this.parentFields.length() > 0) {
            this.parentFields.append(",");
            this.childFields.append(",");
         }

         this.parentFields.append(var1.getParentField());
         this.childFields.append(var1.getChildField());
      }

      public String[] getDeployColumns(String var1) {
         return new String[]{
            var1,
            this.isSource ? "S" : "T",
            this.parentTable,
            this.parentFields.toString(),
            this.childTable,
            this.childFields.toString(),
            this.isOuterJoin ? "1" : "0",
            this.isOneToOne ? "1" : "0"
         };
      }
   }

   private static final class MapKey {
      private final Pair<String, String> data;

      public MapKey(DbTranRelations.Relation var1) {
         this.data = Pair.of(var1.getParentName(), var1.getChildName());
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof TableRelationshipDeployFieldBuilder.MapKey
               ? this.data.equals(((TableRelationshipDeployFieldBuilder.MapKey)var1).data)
               : false;
         }
      }

      @Override
      public int hashCode() {
         return this.data.hashCode();
      }
   }
}
