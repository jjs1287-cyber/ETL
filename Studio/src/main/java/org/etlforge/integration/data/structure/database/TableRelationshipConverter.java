package org.etlforge.integration.data.structure.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

final class TableRelationshipConverter {
   private final List<DbTranRelations.Relation> serverForm;
   private final Map<String, TableRelationship> converted;
   private final DatabaseObject[] objects;

   public TableRelationshipConverter(List<DbTranRelations.Relation> var1, DatabaseStructure var2) {
      this.serverForm = var1;
      this.converted = Maps.newLinkedHashMap();
      this.objects = var2.getDatabaseObjects();
   }

   public TableRelationshipConverter(List<DbTranRelations.Relation> var1, DatabaseObject[] var2) {
      this.serverForm = var1;
      this.objects = var2;
      this.converted = Maps.newLinkedHashMap();
   }

   public List<TableRelationship> convert() {
      for (DbTranRelations.Relation var2 : this.serverForm) {
         DatabaseObject var3 = this.getParentObject(var2);
         if (var3 != null) {
            DatabaseObject var4 = this.getChildObject(var2);
            if (var4 != null) {
               DatabaseColumn var5 = this.getParentField(var3, var2);
               if (var5 != null) {
                  DatabaseColumn var6 = this.getChildField(var4, var2);
                  if (var6 != null) {
                     TableRelationship var7 = this.addToMap(var3, var5, var4, var6);
                     var7.setRelationshipNature(var2.getCardinality());
                  }
               }
            }
         }
      }

      return Lists.newArrayList(this.converted.values());
   }

   private TableRelationship addToMap(DatabaseObject var1, DatabaseColumn var2, DatabaseObject var3, DatabaseColumn var4) {
      String var5 = var1.getName() + "|" + var3.getName();
      TableRelationship var6 = this.converted.get(var5);
      if (var6 == null) {
         var6 = new TableRelationship(var1, var3);
         this.converted.put(var5, var6);
      }

      var6.addFieldRelationship(var2, var4);
      return var6;
   }

   private DatabaseObject getParentObject(DbTranRelations.Relation var1) {
      String var2 = var1.getParentName();
      return this.getObjectImpl(var2);
   }

   private DatabaseObject getChildObject(DbTranRelations.Relation var1) {
      String var2 = var1.getChildName();
      return this.getObjectImpl(var2);
   }

   private DatabaseObject getObjectImpl(String var1) {
      for (DatabaseObject var5 : this.objects) {
         if (var1.equals(var5.getName()) || var1.equals(getQualifiedName(var5))) {
            return var5;
         }
      }

      return null;
   }

   private DatabaseColumn getParentField(DatabaseObject var1, DbTranRelations.Relation var2) {
      String var3 = var2.getParentField();
      return this.getColumn(var1, var3);
   }

   private DatabaseColumn getChildField(DatabaseObject var1, DbTranRelations.Relation var2) {
      String var3 = var2.getChildField();
      return this.getColumn(var1, var3);
   }

   private DatabaseColumn getColumn(DatabaseObject var1, String var2) {
      for (DatabaseColumn var4 : var1.getAllColumns()) {
         if (var2.equals(var4.getName()) || var2.equals(var4.getQualifiedName())) {
            return var4;
         }
      }

      return null;
   }

   private static String getQualifiedName(DatabaseObject var0) {
      return var0.getSchema() + "." + var0.getName();
   }
}
