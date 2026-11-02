package org.etlforge.integration.data.structure.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.info.DatabaseObject;

public final class DatabaseStructureTree {
   private final DatabaseStructureTree.Node root;

   public DatabaseStructureTree(DatabaseStructure var1) {
      this.root = buildTree(var1);
   }

   private static DatabaseStructureTree.Node buildTree(DatabaseStructure var0) {
      return var0.isFlat() ? buildFlatTree(var0) : buildHierarchicalTree(var0);
   }

   private static DatabaseStructureTree.Node buildFlatTree(DatabaseStructure var0) {
      DatabaseObject[] var1 = var0.getDatabaseObjects();
      DatabaseObject var2 = var1[0];
      return new DatabaseStructureTree.Node(var2);
   }

   private static DatabaseStructureTree.Node buildHierarchicalTree(DatabaseStructure var0) {
      DatabaseStructureTree.HierarchicalBuilder var1 = new DatabaseStructureTree.HierarchicalBuilder();
      return var1.build(var0);
   }

   public DatabaseStructureTree.Node getRoot() {
      return this.root;
   }

   private static class HierarchicalBuilder {
      private final Map<String, DatabaseObject> nameToTable = Maps.newHashMap();
      private final Map<String, DatabaseStructureTree.Node> nameToNode = Maps.newHashMap();

      private HierarchicalBuilder() {
      }

      public DatabaseStructureTree.Node build(DatabaseStructure var1) {
         this.buildNameMap(var1);
         DbTranRelations var2 = var1.getRelations();

         for (DbTranRelations.Relation var4 : var2.getRelations()) {
            this.addRelationship(var4);
         }

         return this.findRoot();
      }

      private void buildNameMap(DatabaseStructure var1) {
         DatabaseObject[] var2 = var1.getDatabaseObjects();

         for (DatabaseObject var6 : var2) {
            this.nameToTable.put(var6.getName(), var6);
         }
      }

      private void addRelationship(DbTranRelations.Relation var1) {
         String var2 = var1.getParentName();
         DatabaseStructureTree.Node var3 = this.getNodeForTable(var2);
         String var4 = var1.getChildName();
         DatabaseStructureTree.Node var5 = this.getNodeForTable(var4);
         var3.addChild(var5, var1.getCardinality());
      }

      private DatabaseStructureTree.Node getNodeForTable(String var1) {
         DatabaseStructureTree.Node var2 = this.nameToNode.get(var1);
         if (var2 == null) {
            DatabaseObject var3 = this.nameToTable.get(var1);
            if (var3 == null) {
               throw new RuntimeException("Unknown table: " + var1);
            }

            var2 = new DatabaseStructureTree.Node(var3);
            this.nameToNode.put(var1, var2);
         }

         return var2;
      }

      private DatabaseStructureTree.Node findRoot() {
         for (DatabaseStructureTree.Node var2 : this.nameToNode.values()) {
            if (var2.parent == null) {
               return var2;
            }
         }

         return null;
      }
   }

   public static final class Node {
      private final DatabaseObject table;
      private DatabaseStructureTree.Node parent;
      private final List<DatabaseStructureTree.Node> children;
      private RelationshipCardinality cardinality;

      public Node(DatabaseObject var1) {
         this.table = var1;
         this.children = Lists.newArrayList();
         this.cardinality = RelationshipCardinality.ZERO_TO_N;
      }

      private void addChild(DatabaseStructureTree.Node var1, RelationshipCardinality var2) {
         var1.parent = this;
         var1.cardinality = var2;
         this.children.add(var1);
      }

      public DatabaseObject getTable() {
         return this.table;
      }

      public DatabaseStructureTree.Node getParent() {
         return this.parent;
      }

      public List<DatabaseStructureTree.Node> getChildren() {
         return Lists.newArrayList(this.children);
      }

      public RelationshipCardinality getCardinality() {
         return this.cardinality;
      }

      public boolean hasChildren() {
         return !this.children.isEmpty();
      }

      @Override
      public String toString() {
         return this.table.getName();
      }
   }
}
