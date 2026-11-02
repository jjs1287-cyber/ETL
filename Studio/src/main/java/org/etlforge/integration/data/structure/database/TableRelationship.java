package org.etlforge.integration.data.structure.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public final class TableRelationship {
   static final String RELATIONSHIP_INDICATOR = " \u2014 ";
   private final DatabaseObject parentObject;
   private final DatabaseObject childObject;
   private final Set<FieldRelationship> fields = Sets.newHashSet();
   private RelationshipCardinality nature = RelationshipCardinality.ONE_TO_N;

   public TableRelationship(DatabaseObject var1, DatabaseObject var2) {
      Preconditions.checkNotNull(var1, "parentObject");
      Preconditions.checkNotNull(var1, "childObject");
      Preconditions.checkArgument(var1 != var2, "parentObject and childObject must be different objects.");
      this.parentObject = var1;
      this.childObject = var2;
   }

   public void setRelationshipNature(RelationshipCardinality var1) {
      Preconditions.checkNotNull(var1, "nature");
      this.nature = var1;
   }

   public boolean contains(DatabaseObject var1) {
      return var1 == this.parentObject || var1 == this.childObject;
   }

   public RelationshipCardinality getRelationshipNature() {
      return this.nature;
   }

   public DatabaseObject getParentObject() {
      return this.parentObject;
   }

   public DatabaseObject getChildObject() {
      return this.childObject;
   }

   public void addFieldRelationship(DatabaseColumn var1, DatabaseColumn var2) {
      Preconditions.checkNotNull(var1, "parentColumn");
      Preconditions.checkNotNull(var1, "childColumn");
      Preconditions.checkArgument(var1.getDatabaseObject() == this.parentObject, "parentColumn must belong to the parent object in this relationship");
      Preconditions.checkArgument(var2.getDatabaseObject() == this.childObject, "childColumn must belong to the child object in this relationship");
      FieldRelationship var3 = new FieldRelationship(var1, var2);
      if (!this.fields.contains(var3)) {
         this.fields.add(var3);
      }
   }

   public void setFieldRelationships(Collection<FieldRelationship> var1) {
      this.fields.clear();

      for (FieldRelationship var3 : var1) {
         this.addFieldRelationship(var3.getParentColumn(), var3.getChildColumn());
      }
   }

   public boolean removeFieldRelationship(FieldRelationship var1) {
      return var1 == null ? false : this.fields.remove(var1);
   }

   public int getNumberOfFieldRelationships() {
      return this.fields.size();
   }

   public List<FieldRelationship> getFieldRelationships() {
      return Lists.newArrayList(this.fields);
   }

   public boolean containsFieldRelationship(DatabaseColumn var1, DatabaseColumn var2) {
      for (FieldRelationship var4 : this.fields) {
         if (var4.getParentColumn().equals(var1) && var4.getChildColumn().equals(var2)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public String toString() {
      return this.parentObject.getName() + " \u2014 " + this.childObject.getName();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 instanceof TableRelationship) {
         TableRelationship var2 = (TableRelationship)var1;
         return !this.parentObject.equals(var2.parentObject) ? false : this.childObject.equals(var2.childObject);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 37 * var1 + this.parentObject.hashCode();
      return 37 * var1 + this.childObject.hashCode();
   }
}
