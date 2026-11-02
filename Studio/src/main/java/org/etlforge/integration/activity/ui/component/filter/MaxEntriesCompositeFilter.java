package org.etlforge.integration.activity.ui.component.filter;

import java.util.List;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.MaxEntriesToGetFilter;

public class MaxEntriesCompositeFilter<T extends ActivityEntry> extends CompositeEntryFilterUi<T> {
   private final MaxEntriesToGetFilterUi<T> maxEntriesToGetFilter;

   public MaxEntriesCompositeFilter(String var1, int var2) {
      super(var1);
      this.maxEntriesToGetFilter = new MaxEntriesToGetFilterUi<>(var2);
      this.addFilter(this.maxEntriesToGetFilter);
   }

   public final MaxEntriesToGetFilter getMaxEntriesToGetFilter() {
      return this.maxEntriesToGetFilter.getFilter();
   }

   protected final MaxEntriesToGetFilterUi getMaxEntriesToGetFilterUi() {
      return this.maxEntriesToGetFilter;
   }

   @Override
   public List<T> getFilteredList(List<T> var1) {
      List var2 = super.getFilteredList(var1);
      return this.getMaxEntriesToGetFilter().getFilteredList(var2);
   }
}
