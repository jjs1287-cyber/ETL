package org.etlforge.integration.salesforce;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.etlforge.xml.ExpandedName;

public final class SObject implements Serializable {
   private static final long serialVersionUID = -4561389916169077049L;
   public static final String FIELDS_TO_NULL = "fieldsToNull";
   public static final Comparator<SObject.Field> FIELD_NAME_ORDER = new SObject.Order();
   private final ExpandedName name;
   private final Set<SObject.Field> fields;

   public SObject(ExpandedName var1, Collection<SObject.Field> var2) {
      this.name = var1;
      this.fields = Sets.newHashSet(var2);
   }

   public ExpandedName getName() {
      return this.name;
   }

   public String getLocalName() {
      return this.name.getLocalName();
   }

   public SortedSet<SObject.Field> getFields() {
      TreeSet var1 = Sets.newTreeSet(FIELD_NAME_ORDER);
      var1.addAll(this.fields);
      return var1;
   }

   @Override
   public String toString() {
      return this.getLocalName();
   }

   public static final class Field implements Serializable {
      private static final long serialVersionUID = -4299976196998348858L;
      private static final Set<String> SYSTEM_FIELDS = Sets.newHashSet(
         "Id", "IsDeleted", "CreatedById", "CreatedDate", "LastActivityDate", "LastModifiedById", "LastModifiedDate", "MasterRecordId", "SystemModstamp"
      );
      private final String name;
      private final ExpandedName typeName;
      private final boolean simple;

      public Field(String var1, ExpandedName var2) {
         this.name = var1;
         this.typeName = var2;
         this.simple = SimpleTypes.isSimple(var2);
      }

      public String getName() {
         return this.name;
      }

      public ExpandedName getTypeName() {
         return this.typeName;
      }

      public boolean isSimpleType() {
         return this.simple;
      }

      public boolean isSystemField() {
         return SYSTEM_FIELDS.contains(this.name);
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this ? true : var1 instanceof SObject.Field && this.name.equals(((SObject.Field)var1).name);
      }

      @Override
      public int hashCode() {
         return this.name.hashCode();
      }

      @Override
      public String toString() {
         return this.name;
      }
   }

   private static final class Order implements Comparator<SObject.Field>, Serializable {
      private Order() {
      }

      public int compare(SObject.Field var1, SObject.Field var2) {
         return var1.getName().compareToIgnoreCase(var2.getName());
      }
   }
}
