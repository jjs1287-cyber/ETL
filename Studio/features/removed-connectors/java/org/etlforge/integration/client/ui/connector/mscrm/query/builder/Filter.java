package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.etlforge.xml.XmlStringBuilder;

final class Filter implements FilterEntry {
   private final FilterOperator operator;
   private final ImmutableList<FilterEntry> entries;

   public static Filter.FilterBuilder builder(FilterOperator var0) {
      return new Filter.FilterBuilder(Preconditions.checkNotNull(var0, "op"));
   }

   public static Filter.FilterBuilder and() {
      return new Filter.FilterBuilder(FilterOperator.AND);
   }

   public static Filter.FilterBuilder or() {
      return new Filter.FilterBuilder(FilterOperator.OR);
   }

   public Filter(FilterOperator var1, Iterable<FilterEntry> var2) {
      this(var1, ImmutableList.copyOf(var2));
   }

   public Filter(FilterOperator var1, ImmutableList<FilterEntry> var2) {
      this.operator = Preconditions.checkNotNull(var1, "operator");
      this.entries = Preconditions.checkNotNull(var2, "entries");
   }

   public FilterOperator getOperator() {
      return this.operator;
   }

   public ImmutableList<FilterEntry> getEntries() {
      return this.entries;
   }

   @Override
   public void appendToXml(XmlStringBuilder var1) {
      if (!this.entries.isEmpty()) {
         var1.addElement("filter");
         var1.addAttribute("type", this.operator.getXml());

         for (FilterEntry var3 : this.entries) {
            var3.appendToXml(var1);
         }

         var1.closeElement();
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.operator, this.entries);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Filter)) {
         return false;
      }

      Filter var2 = (Filter)var1;
      return this.operator == var2.operator && this.entries.equals(var2.entries);
   }

   @Override
   public String toString() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      this.appendToXml(var1);
      return var1.getXml();
   }

   public static final class FilterBuilder {
      private final FilterOperator operator;
      private final Builder<FilterEntry> entries = ImmutableList.builder();

      private FilterBuilder(FilterOperator var1) {
         this.operator = var1;
      }

      public Filter.FilterBuilder addEntry(FilterEntry var1) {
         this.entries.add(var1);
         return this;
      }

      public Filter.FilterBuilder addEntries(FilterEntry var1) {
         this.entries.add(var1);
         return this;
      }

      public Filter.FilterBuilder addEntries(Iterable<? extends FilterEntry> var1) {
         this.entries.addAll(var1);
         return this;
      }

      public Filter build() {
         return new Filter(this.operator, this.entries.build());
      }
   }
}
