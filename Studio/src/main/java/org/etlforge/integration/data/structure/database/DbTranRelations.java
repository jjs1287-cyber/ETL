package org.etlforge.integration.data.structure.database;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import org.etlforge.collections.Pair;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.annotations.InternalMethod;

public final class DbTranRelations implements Cloneable {
   public static final char TYPE_SOURCE = 'S';
   public static final char TYPE_TARGET = 'T';
   public static final String DOC_TYPE = "doc_type";
   public static final String PARENT_NAME = "parent_name";
   public static final String PARENT_FIELD = "parent_field";
   public static final String CHILD_NAME = "child_name";
   public static final String CHILD_FIELD = "child_field";
   public static final String OUTER_JOIN = "outer_join";
   public static final String ONE_TO_ONE = "onetoone";
   private DbTranRelations.OrderedRelations relations = new DbTranRelations.OrderedRelations();

   @InternalMethod
   public void addRelation(DbTranRelations.Relation var1) {
      this.relations.add(var1);
   }

   public List<DbTranRelations.Relation> getRelations() {
      return this.relations.getRelationsWithParentChildOrderIntact();
   }

   public void addTableRelationship(TableRelationship var1, boolean var2) {
      for (FieldRelationship var4 : var1.getFieldRelationships()) {
         DbTranRelations.Relation var5 = new DbTranRelations.Relation(var1, var4);
         var5.setIsSource(var2);
         this.relations.add(var5);
      }
   }

   public List<TableRelationship> getTableRelationships(DatabaseObject[] var1) {
      Preconditions.checkNotNull(var1, "objs");
      TableRelationshipConverter var2 = new TableRelationshipConverter(this.relations.getRelationsWithParentChildOrderIntact(), var1);
      return var2.convert();
   }

   public List<TableRelationship> getTableRelationships(DatabaseStructure var1) {
      Preconditions.checkNotNull(var1, "dbStruct");
      TableRelationshipConverter var2 = new TableRelationshipConverter(this.relations.getRelationsWithParentChildOrderIntact(), var1);
      return var2.convert();
   }

   public void clear() {
      this.relations.clear();
   }

   public DbTranRelations getCopy() {
      try {
         DbTranRelations var1 = (DbTranRelations)this.clone();
         var1.relations = this.relations.copy();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String getRootName() {
      HashSet var1 = Sets.newHashSet();
      HashSet var2 = Sets.newHashSet();

      for (DbTranRelations.Relation var4 : this.relations.getRelationsInArbitraryOrder()) {
         String var5 = var4.getChildName();
         var2.add(var5);
         var1.remove(var5);
         String var6 = var4.getParentName();
         if (!var2.contains(var6)) {
            var1.add(var6);
         }
      }

      return var1.size() == 1 ? (String)var1.iterator().next() : null;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof DbTranRelations ? this.relations.equals(((DbTranRelations)var1).relations) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.relations.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for (DbTranRelations.Relation var4 : this.getRelations()) {
         if (var2 > 0) {
            var1.append("\n");
         }

         var1.append(var4);
         var2++;
      }

      return var1.toString();
   }

   private static final class OrderedRelations {
      private static final Comparator<Pair<String, String>> PAIR_ORDER = new Comparator<Pair<String, String>>() {
         public int compare(Pair<String, String> var1, Pair<String, String> var2) {
            String var3 = (String)var1.first + "|" + (String)var1.second;
            String var4 = (String)var2.first + "|" + (String)var2.second;
            return var3.compareTo(var4);
         }
      };
      private final SortedMap<String, LinkedHashSet<String>> childOrder = Maps.newTreeMap();
      private final SortedMap<Pair<String, String>, Set<DbTranRelations.Relation>> relations = Maps.newTreeMap(PAIR_ORDER);

      public OrderedRelations() {
      }

      public List<DbTranRelations.Relation> getRelationsWithParentChildOrderIntact() {
         ArrayList var1 = Lists.newArrayList();

         for (Entry var3 : this.childOrder.entrySet()) {
            String var4 = (String)var3.getKey();

            for (Object __etlItem147 : (LinkedHashSet)var3.getValue()) {
               String var6 = (String) __etlItem147;
               var1.addAll(this.relations.get(Pair.of(var4, var6)));
            }
         }

         return var1;
      }

      public Set<DbTranRelations.Relation> getRelationsInArbitraryOrder() {
         HashSet var1 = Sets.newHashSet();

         for (Set var3 : this.relations.values()) {
            var1.addAll(var3);
         }

         return var1;
      }

      public void add(DbTranRelations.Relation var1) {
         String var2 = var1.getParentName();
         String var3 = var1.getChildName();
         this.addChildToParent(var2, var3);
         this.addParentChildRelation(var1, var2, var3);
      }

      private void addChildToParent(String var1, String var2) {
         LinkedHashSet var3 = this.childOrder.get(var1);
         if (var3 == null) {
            var3 = Sets.newLinkedHashSet();
            this.childOrder.put(var1, var3);
         }

         var3.add(var2);
      }

      private void addParentChildRelation(DbTranRelations.Relation var1, String var2, String var3) {
         Pair var4 = Pair.of(var2, var3);
         Set var5 = this.relations.get(var4);
         if (var5 == null) {
            var5 = Sets.newHashSet();
            this.relations.put(var4, var5);
         }

         var5.add(var1);
      }

      public void clear() {
         this.childOrder.clear();
         this.relations.clear();
      }

      public DbTranRelations.OrderedRelations copy() {
         DbTranRelations.OrderedRelations var1 = new DbTranRelations.OrderedRelations();

         for (DbTranRelations.Relation var3 : this.getRelationsWithParentChildOrderIntact()) {
            var1.add(var3.getCopy());
         }

         return var1;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof DbTranRelations.OrderedRelations)) {
            return false;
         }

         DbTranRelations.OrderedRelations var2 = (DbTranRelations.OrderedRelations)var1;
         return this.childOrder.equals(var2.childOrder) && this.relations.equals(var2.relations);
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + this.childOrder.hashCode();
         return 31 * var1 + this.relations.hashCode();
      }
   }

   public static final class Relation implements Comparable<DbTranRelations.Relation>, Cloneable {
      private static long TEMPORARY_ID_SEED = -System.currentTimeMillis();
      private long id;
      private final String parentName;
      private final String parentField;
      private final String childName;
      private final String childField;
      private final boolean outerJoin;
      private final boolean oneToOne;
      private boolean isSource;

      public Relation(TableRelationship var1, FieldRelationship var2) {
         this(
            getNextTemporaryId(),
            var1.getParentObject().getName(),
            var2.getParentColumn().getName(),
            var1.getChildObject().getName(),
            var2.getChildColumn().getName(),
            var1.getRelationshipNature().isOuterJoin(),
            isOneToOne(var1.getRelationshipNature())
         );
      }

      public Relation(long var1, String var3, String var4, String var5, String var6, boolean var7, boolean var8) {
         this.id = var1;
         this.parentName = var3;
         this.parentField = var4;
         this.childName = var5;
         this.childField = var6;
         this.outerJoin = var7;
         this.oneToOne = var8;
      }

      @Override
      public String toString() {
         StringBuilder var1 = new StringBuilder("P=");
         var1.append(this.parentName);
         var1.append("  PField=");
         var1.append(this.parentField);
         var1.append("    C=");
         var1.append(this.childName);
         var1.append("  CField=");
         var1.append(this.childField);
         var1.append("    OJ: ");
         var1.append(this.outerJoin);
         var1.append("  1-1: ");
         var1.append(this.oneToOne);
         return var1.toString();
      }

      public void setIsSource(boolean var1) {
         this.isSource = var1;
      }

      public boolean isSource() {
         return this.isSource;
      }

      public long getId() {
         return this.id;
      }

      public void setId(long var1) {
         this.id = var1;
      }

      public String getParentName() {
         return this.parentName;
      }

      public String getParentField() {
         return this.parentField;
      }

      public String getChildName() {
         return this.childName;
      }

      public String getChildField() {
         return this.childField;
      }

      public boolean isOuterJoin() {
         return this.outerJoin;
      }

      public boolean isOneToOne() {
         return this.oneToOne;
      }

      public RelationshipCardinality getCardinality() {
         if (this.outerJoin) {
            return this.oneToOne ? RelationshipCardinality.ZERO_OR_ONE : RelationshipCardinality.ZERO_TO_N;
         } else {
            return this.oneToOne ? RelationshipCardinality.ONE_TO_ONE : RelationshipCardinality.ONE_TO_N;
         }
      }

      public static String[] getColumnNames() {
         return new String[]{"transformation_id", "doc_type", "parent_name", "parent_field", "child_name", "child_field", "outer_join", "onetoone"};
      }

      public static void addFromColumnValues(DbTranRelations var0, StringKeyValuePairs var1) {
         String[] var2 = KongaStringUtils.split(var1.get("parent_field"), ",", '\\');
         String[] var3 = KongaStringUtils.split(var1.get("child_field"), ",", '\\');
         if (var2.length != var3.length) {
            throw new IllegalArgumentException(
               "Inconsistent download data. parentFields and childFields are of different sizes: parentFields = '"
                  + var1.get("parent_field")
                  + "', childFields = '"
                  + var1.get("child_field")
                  + "'."
            );
         }

         for (int var4 = 0; var4 < var2.length; var4++) {
            String var5 = var2[var4];
            String var6 = var3[var4];
            DbTranRelations.Relation var7 = new DbTranRelations.Relation(
               getNextTemporaryId(), var1.get("parent_name"), var5, var1.get("child_name"), var6, var1.getBoolean("outer_join"), var1.getBoolean("onetoone")
            );
            String var8 = String.valueOf('S');
            var7.setIsSource(var8.equalsIgnoreCase(var1.getString("doc_type")));
            var0.addRelation(var7);
         }
      }

      public DbTranRelations.Relation getCopy() {
         try {
            return (DbTranRelations.Relation)this.clone();
         } catch (CloneNotSupportedException var2) {
            throw new RuntimeException(var2);
         }
      }

      public int compareTo(DbTranRelations.Relation var1) {
         return Longs.compare(this.id, var1.id);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 instanceof DbTranRelations.Relation) {
            DbTranRelations.Relation var2 = (DbTranRelations.Relation)var1;
            if (this.id != var2.id) {
               return false;
            } else if (this.outerJoin != var2.outerJoin) {
               return false;
            } else if (this.oneToOne != var2.oneToOne) {
               return false;
            } else if (!Objects.equal(this.parentName, var2.parentName)) {
               return false;
            } else if (!Objects.equal(this.parentField, var2.parentField)) {
               return false;
            } else {
               return !Objects.equal(this.childName, var2.childName) ? false : Objects.equal(this.childField, var2.childField);
            }
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 37 * var1 + Longs.hashCode(this.id);
         var1 = 37 * var1 + (this.parentName == null ? 0 : this.parentName.hashCode());
         var1 = 37 * var1 + (this.parentField == null ? 0 : this.parentField.hashCode());
         var1 = 37 * var1 + (this.childName == null ? 0 : this.childName.hashCode());
         var1 = 37 * var1 + (this.childField == null ? 0 : this.childField.hashCode());
         var1 = 37 * var1 + (this.outerJoin ? 1 : 0);
         return 37 * var1 + (this.oneToOne ? 1 : 0);
      }

      private static boolean isOneToOne(RelationshipCardinality var0) {
         return var0 == RelationshipCardinality.ZERO_OR_ONE || var0 == RelationshipCardinality.ONE_TO_ONE;
      }

      private static synchronized long getNextTemporaryId() {
         return --TEMPORARY_ID_SEED;
      }
   }
}
