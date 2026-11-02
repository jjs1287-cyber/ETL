package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.etlforge.integration.salesforce.parser.ParsedQuery;

final class FieldList implements Iterable<FieldWrapper> {
   private final ImmutableList<FieldWrapper> fields;

   public FieldList(ParsedQuery var1) {
      Builder var2 = ImmutableList.builder();

      for (String var4 : var1.getFieldNames()) {
         var2.add(new FieldWrapper(var1, var4));
      }

      this.fields = var2.build();
   }

   @Override
   public Iterator<FieldWrapper> iterator() {
      return this.fields.iterator();
   }

   public boolean isEmpty() {
      return this.fields.isEmpty();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof FieldList && this.fields.equals(((FieldList)var1).fields);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("fields", this.fields).toString();
   }

   @Override
   public int hashCode() {
      return this.fields.hashCode();
   }

   public ImmutableSet<FieldWrapper> inThisButNotInThat(FieldList var1) {
      Set var2 = this.asSet();
      var2.removeAll(var1.asSet());
      return ImmutableSet.copyOf(var2);
   }

   private Set<FieldWrapper> asSet() {
      return Sets.newHashSet(this.fields);
   }

   public ImmutableMap<FieldWrapper, FieldWrapper> inThisAndInThat(FieldList var1) {
      com.google.common.collect.ImmutableMap.Builder var2 = ImmutableMap.builder();
      Map var3 = var1.asMap();

      for (FieldWrapper var5 : this.fields) {
         FieldWrapper var6 = (FieldWrapper)var3.get(var5.getSimpleSfdcFieldName());
         if (var6 != null) {
            var2.put(var5, var6);
         }
      }

      return var2.build();
   }

   private Map<String, FieldWrapper> asMap() {
      TreeMap var1 = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);

      for (FieldWrapper var3 : this.fields) {
         var1.put(var3.getSimpleSfdcFieldName(), var3);
      }

      return var1;
   }
}
