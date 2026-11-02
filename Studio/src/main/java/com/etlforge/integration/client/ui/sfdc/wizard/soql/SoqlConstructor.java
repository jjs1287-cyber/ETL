package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Collections;
import org.etlforge.integration.salesforce.wsdl.Fields;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.name.Nameables;

final class SoqlConstructor {
   private final String sobject;
   private final ImmutableList<SObjectSchema.Field> fields;
   private final ImmutableList<ChildToParentField> childToParentFields;
   private final ImmutableList<NestedQuery> nestedQueries;
   private final ImmutableList<Condition> conditions;

   public SoqlConstructor(String var1, Collection<SObjectSchema.Field> var2, Collection<Condition> var3) {
      this(var1, var2, Collections.EMPTY_LIST, Collections.EMPTY_LIST, var3);
   }

   public SoqlConstructor(
      String var1, Iterable<SObjectSchema.Field> var2, Iterable<ChildToParentField> var3, Iterable<NestedQuery> var4, Iterable<Condition> var5
   ) {
      this.sobject = var1;
      this.fields = Fields.ORDER.immutableSortedCopy(var2);
      this.childToParentFields = Ordering.natural().immutableSortedCopy(var3);
      this.nestedQueries = NestedQuery.ORDER.immutableSortedCopy(var4);
      this.conditions = ImmutableList.copyOf(var5);
   }

   public String buildSoql() {
      StringBuilder var1 = new StringBuilder("SELECT ");
      this.appendFields(var1);
      this.appendNestedQueries(var1);
      if (!this.fields.isEmpty() || !this.childToParentFields.isEmpty() || !this.nestedQueries.isEmpty()) {
         var1.append(" ");
      }

      var1.append("FROM ").append(this.sobject).append(" ");
      if (!this.conditions.isEmpty()) {
         var1.append("WHERE ").append(this.getWhereClause());
      }

      return var1.toString();
   }

   private void appendFields(StringBuilder var1) {
      if (!this.fields.isEmpty()) {
         joinAndAppend(var1, this.fields, Nameables.toNameFunction());
      }

      if (!this.childToParentFields.isEmpty()) {
         if (!this.fields.isEmpty()) {
            var1.append(", ");
         }

         joinAndAppend(var1, this.childToParentFields, new Function<ChildToParentField, String>() {
            public String apply(ChildToParentField var1) {
               return var1.toSoql();
            }
         });
      }
   }

   private static <T> void joinAndAppend(StringBuilder var0, Iterable<T> var1, Function<? super T, String> var2) {
      Joiner.on(", ").appendTo(var0, Iterables.transform(var1, var2));
   }

   private void appendNestedQueries(StringBuilder var1) {
      if (!this.nestedQueries.isEmpty()) {
         if (!this.fields.isEmpty() || !this.childToParentFields.isEmpty()) {
            var1.append(", ");
         }

         joinAndAppend(var1, this.nestedQueries, new Function<NestedQuery, String>() {
            public String apply(NestedQuery var1) {
               return var1.toSoql();
            }
         });
      }
   }

   private String getWhereClause() {
      Collection var1 = Collections2.transform(this.conditions, Condition.TO_SOQL);
      return Joiner.on(" AND ").join(var1);
   }
}
