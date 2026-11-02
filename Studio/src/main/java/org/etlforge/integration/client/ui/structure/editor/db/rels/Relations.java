package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.structure.database.FieldRelationship;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseObject;

public final class Relations {
   private final List<TableRelationship> relations = Lists.newArrayList();
   private final Map<DatabaseObject, DatabaseObject> childToParent = Maps.newHashMap();

   public void add(TableRelationship var1) {
      this.validateNewRelation(var1);
      this.addRelation(var1);
   }

   private void validateNewRelation(TableRelationship var1) {
      if (this.isAncestor(var1.getChildObject(), var1.getParentObject())) {
         throw new IllegalRelationException(var1.getChildObject().getName() + " is an ancestor of " + var1.getParentObject().getName());
      }

      DatabaseObject var2 = var1.getChildObject();

      for (TableRelationship var4 : this.relations) {
         if (var4.getChildObject() == var2) {
            if (var4.getParentObject() != var1.getParentObject()) {
               throw new IllegalRelationException(var2.getName() + " is already linked to another parent table");
            }

            this.remove(var4);
            break;
         }
      }
   }

   private void addRelation(TableRelationship var1) {
      this.relations.add(var1);
      this.childToParent.put(var1.getChildObject(), var1.getParentObject());
   }

   public void remove(TableRelationship var1) {
      this.relations.remove(var1);
      this.childToParent.remove(var1.getChildObject());
   }

   public void removeTable(DatabaseObject var1) {
      ArrayList var2 = Lists.newArrayList();

      for (TableRelationship var4 : Lists.newArrayList(this.relations)) {
         if (var4.contains(var1)) {
            this.remove(var4);
            if (var4.getParentObject() == var1) {
               var2.add(var4.getChildObject());
            }
         }
      }

      for (Object __etlItem66 : var2) {
         DatabaseObject var6 = (DatabaseObject) __etlItem66;
         this.removeTable(var6);
      }
   }

   public void removeFromParent(DatabaseObject var1) {
      DatabaseObject var2 = this.getParent(var1);
      if (var2 != null) {
         TableRelationship var3 = this.getRelation(var2, var1);
         this.remove(var3);
      }
   }

   public List<FieldRelationship> getLinkKeys(DatabaseObject var1, DatabaseObject var2) {
      for (TableRelationship var4 : this.relations) {
         if (var4.getParentObject() == var1 && var4.getChildObject() == var2) {
            return var4.getFieldRelationships();
         }
      }

      return Lists.newArrayList();
   }

   public TableRelationship getRelation(DatabaseObject var1, DatabaseObject var2) {
      for (TableRelationship var4 : this.relations) {
         if (var4.getParentObject() == var1 && var4.getChildObject() == var2) {
            return var4;
         }
      }

      return null;
   }

   public List<TableRelationship> getAllRelationships() {
      return Lists.newArrayList(this.relations);
   }

   public boolean isEmpty() {
      return this.relations.isEmpty();
   }

   public boolean usesAllObjects(Collection<? extends DatabaseObject> var1) {
      HashSet var2 = Sets.newHashSet(var1);

      for (TableRelationship var4 : this.relations) {
         var2.remove(var4.getParentObject());
         var2.remove(var4.getChildObject());
      }

      return var2.isEmpty();
   }

   public boolean hasSingleRoot() {
      Set var1 = this.getAllObjects();

      for (TableRelationship var3 : this.relations) {
         var1.remove(var3.getChildObject());
      }

      return var1.size() == 1;
   }

   public DatabaseObject getParent(DatabaseObject var1) {
      return this.childToParent.get(var1);
   }

   public boolean isAncestor(DatabaseObject var1, DatabaseObject var2) {
      for (DatabaseObject var3 = this.childToParent.get(var2); var3 != null; var3 = this.childToParent.get(var3)) {
         if (var3 == var1) {
            return true;
         }
      }

      return false;
   }

   private Set<DatabaseObject> getAllObjects() {
      HashSet var1 = Sets.newHashSet();

      for (TableRelationship var3 : this.relations) {
         var1.add(var3.getParentObject());
         var1.add(var3.getChildObject());
      }

      return var1;
   }
}
