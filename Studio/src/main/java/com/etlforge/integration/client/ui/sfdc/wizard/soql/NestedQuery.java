package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.ArgCheck;

public final class NestedQuery {
   private final String objectName;
   private final ImmutableSet<SObjectSchema.Field> fields;
   private final ImmutableSet<ChildToParentField> childToParentFields;
   public static final Ordering<NestedQuery> ORDER = Ordering.from(new Comparator<NestedQuery>() {
      public int compare(NestedQuery var1, NestedQuery var2) {
         return var1.getObjectName().compareToIgnoreCase(var2.getObjectName());
      }
   });

   public static NestedQuery empty(String var0) {
      return new NestedQuery(ArgCheck.checkNotBlank(var0));
   }

   public NestedQuery(String var1, Iterable<SObjectSchema.Field> var2, Iterable<ChildToParentField> var3) {
      this.objectName = ArgCheck.checkNotBlank(var1);
      this.fields = ImmutableSet.copyOf(var2);
      this.childToParentFields = ImmutableSet.copyOf(var3);
   }

   private NestedQuery(String var1) {
      this.objectName = var1;
      this.fields = ImmutableSet.of();
      this.childToParentFields = ImmutableSet.of();
   }

   public String getObjectName() {
      return this.objectName;
   }

   public ImmutableSet<SObjectSchema.Field> getFields() {
      return this.fields;
   }

   public ImmutableSet<ChildToParentField> getChildToParentFields() {
      return this.childToParentFields;
   }

   public boolean isEmpty() {
      return this.fields.isEmpty() && this.childToParentFields.isEmpty();
   }

   public NestedQuery addField(SObjectSchema.Field var1) {
      return this.fields.contains(var1) ? this : new NestedQuery(this.objectName, copySetAndAdd(this.fields, var1), this.childToParentFields);
   }

   private static <T> Set<T> copySetAndAdd(Set<T> var0, T var1) {
      HashSet var2 = Sets.newHashSet(var0);
      var2.add(var1);
      return var2;
   }

   public NestedQuery removeField(SObjectSchema.Field var1) {
      return !this.fields.contains(var1) ? this : new NestedQuery(this.objectName, copySetAndRemove(this.fields, var1), this.childToParentFields);
   }

   private static <T> Set<T> copySetAndRemove(Set<T> var0, T var1) {
      HashSet var2 = Sets.newHashSet();
      var2.remove(var1);
      return var2;
   }

   public NestedQuery addChildToParentField(ChildToParentField var1) {
      return this.childToParentFields.contains(var1) ? this : new NestedQuery(this.objectName, this.fields, copySetAndAdd(this.childToParentFields, var1));
   }

   public NestedQuery removeChildToParentField(ChildToParentField var1) {
      return !this.childToParentFields.contains(var1) ? this : new NestedQuery(this.objectName, this.fields, copySetAndRemove(this.childToParentFields, var1));
   }

   public String toSoql() {
      SoqlConstructor var1 = new SoqlConstructor(this.objectName, this.fields, this.childToParentFields, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      return "(" + var1.buildSoql().trim() + ")";
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.objectName, this.fields, this.childToParentFields);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof NestedQuery)) {
         return false;
      }

      NestedQuery var2 = (NestedQuery)var1;
      return this.objectName.equals(var2.objectName) && this.fields.equals(var2.fields) && this.childToParentFields.equals(var2.childToParentFields);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).addValue(this.toSoql()).toString();
   }
}
