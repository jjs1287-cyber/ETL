package org.etlforge.integration.client.entity.search;

import com.google.common.collect.ImmutableList;

public class SearchAndReplaceMetaData {
   private final ImmutableList<? extends SearchableProperty> properties;
   public static final SearchAndReplaceMetaData EMPTY = new SearchAndReplaceMetaData();

   public SearchAndReplaceMetaData(SearchableProperty... var1) {
      this.properties = ImmutableList.copyOf(var1);
   }

   public SearchAndReplaceMetaData(ImmutableList<? extends SearchableProperty> var1) {
      this.properties = var1;
   }

   public final boolean hasProperties() {
      return !this.properties.isEmpty();
   }

   public final ImmutableList<? extends SearchableProperty> getSearchableProperties() {
      return this.properties;
   }

   public boolean includeName() {
      return true;
   }
}
