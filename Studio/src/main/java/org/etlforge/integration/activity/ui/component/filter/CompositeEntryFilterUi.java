package org.etlforge.integration.activity.ui.component.filter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;
import org.etlforge.integration.activity.client.filter.OperationNameFilter;
import org.etlforge.integration.activity.ui.component.filter.displayer.FilterUiDisplayerLauncher;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.border.BorderBuilder;

public class CompositeEntryFilterUi<T extends ActivityEntry> extends AbstractActivityEntryFilterUi<T> {
   private final String title;
   private FilterUiDisplayerLauncher displayerLauncher;
   private final List<ActivityEntryFilterUi<T>> filterUis;
   private final ActivityEntryFilter filter;
   private final FilterListener subFilterListener;
   private final OperationNameFilterUi<T> nameFilter;

   public CompositeEntryFilterUi(String var1) {
      this.title = var1;
      this.filterUis = Lists.newArrayList();
      this.filter = new CompositeEntryFilterUi.FilterImpl();
      this.subFilterListener = new CompositeEntryFilterUi.SubFilterListener();
      this.nameFilter = new OperationNameFilterUi<>();
      this.addFilter(this.nameFilter);
   }

   public final OperationNameFilter getNameFilter() {
      return this.nameFilter.getFilter();
   }

   protected final OperationNameFilterUi<T> getNameFilterUi() {
      return this.nameFilter;
   }

   @Override
   public final ActivityEntryFilter getFilter() {
      return this.filter;
   }

   public final void addFilter(ActivityEntryFilterUi<T> var1) {
      Preconditions.checkNotNull(var1, "f");
      var1.addFilterListener(this.subFilterListener);
      this.filterUis.add(var1);
   }

   @Override
   public final void apply() {
      for (ActivityEntryFilterUi var2 : this.filterUis) {
         var2.apply();
      }
   }

   @Override
   public final void reset() {
      for (ActivityEntryFilterUi var2 : this.filterUis) {
         var2.reset();
      }
   }

   @Override
   public List<T> getFilteredList(List<T> var1) {
      super.getFilteredList(var1);
      if (!var1.isEmpty() && !this.filterUis.isEmpty() && this.isActive()) {
         ArrayList var2 = new ArrayList();

         for (ActivityEntry var4 : var1) {
            if (this.getFilter().accepts(var4)) {
               var2.add(var4);
            }
         }

         return var2;
      } else {
         return var1;
      }
   }

   @Override
   public final String getFilterString() {
      StringBuilder var1 = new StringBuilder();

      for (ActivityEntryFilterUi var3 : this.filterUis) {
         String var4 = var3.getFilterString();
         if (var4 != null && var4.length() > 0) {
            var1.append(var4);
            var1.append("; ");
         }
      }

      return var1.toString();
   }

   @Override
   public final void requestFocus() {
      if (!this.filterUis.isEmpty()) {
         this.filterUis.get(0).requestFocus();
      }
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(this.createControlPanel());
   }

   private JComponent createControlPanel() {
      BoxBuilder var1 = Vertical.box().leftAligned();
      var1.addAllWithSpace(10, this.filterUis);
      if (this.useScrollPane()) {
         return this.wrapInScrollPane(var1);
      }

      var1.withBorder(new BorderBuilder().line().empty(9));
      return var1.container();
   }

   private JComponent wrapInScrollPane(BoxBuilder var1) {
      return var1.deeplyNonOpaque().withEmptyBorder(10).inScroll().makeInconspicuous();
   }

   protected boolean useScrollPane() {
      return false;
   }

   public final void decorateLauncher(UiCanvas var1) {
      if (this.displayerLauncher == null) {
         this.displayerLauncher = this.createLauncher();
      }

      this.displayerLauncher.getUi().decorate(var1);
   }

   private FilterUiDisplayerLauncher createLauncher() {
      return new FilterUiDisplayerLauncher<>(this, new CompositeEntryFilterUi.DisplayerListener(), this.title);
   }

   @Override
   public final boolean isActive() {
      for (ActivityEntryFilterUi var2 : this.filterUis) {
         if (var2.isActive()) {
            return true;
         }
      }

      return false;
   }

   private final class DisplayerListener implements FilterListener {
      private DisplayerListener() {
      }

      @Override
      public void filterChanged(ActivityEntryFilterUi<?> var1) {
         CompositeEntryFilterUi.this.fireFilterChanged(CompositeEntryFilterUi.this);
      }
   }

   private final class FilterImpl extends ActivityEntryFilter {
      private FilterImpl() {
      }

      @Override
      public boolean accepts(ActivityEntry var1) {
         for (ActivityEntryFilterUi var3 : CompositeEntryFilterUi.this.filterUis) {
            if (var3.isActive() && !var3.getFilter().accepts(var1)) {
               return false;
            }
         }

         return true;
      }
   }

   private final class SubFilterListener implements FilterListener {
      private SubFilterListener() {
      }

      @Override
      public void filterChanged(ActivityEntryFilterUi<?> var1) {
         CompositeEntryFilterUi.this.fireFilterChanged(CompositeEntryFilterUi.this);
      }
   }
}
