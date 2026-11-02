package org.etlforge.integration.activity.ui.component.filter;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;
import org.etlforge.ui.util.Orientation;

public abstract class AbstractActivityEntryFilterUi<T extends ActivityEntry> implements ActivityEntryFilterUi<T> {
   private final CopyOnWriteArrayList<FilterListener> listeners = new CopyOnWriteArrayList<>();
   private Orientation orientation = Orientation.VERTICAL;

   public List<T> getFilteredList(List<T> var1) {
      if (!var1.isEmpty() && this.isActive()) {
         ArrayList var2 = new ArrayList();

         for (ActivityEntry var4 : var1) {
            ActivityEntryFilter var5 = this.getFilter();
            if (var5 == null || var5.accepts(var4)) {
               var2.add(var4);
            }
         }

         return var2;
      } else {
         return var1;
      }
   }

   @Override
   public final void addFilterListener(FilterListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public final void removeFilterListener(FilterListener var1) {
      this.listeners.remove(var1);
   }

   protected final void fireFilterChanged(ActivityEntryFilterUi<?> var1) {
      if (this.listeners != null) {
         for (FilterListener var3 : this.listeners) {
            var3.filterChanged(var1);
         }
      }
   }

   public final void setOrientation(Orientation var1) {
      Preconditions.checkNotNull(var1, "o");
      this.orientation = var1;
   }

   protected final int getCapsuleOrientation() {
      return this.orientation == Orientation.VERTICAL ? 2 : 0;
   }
}
