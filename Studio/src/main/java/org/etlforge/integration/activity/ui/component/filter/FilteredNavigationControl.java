package org.etlforge.integration.activity.ui.component.filter;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JPanel;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.ui.component.navigation.NavigationControl;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public abstract class FilteredNavigationControl<T extends ActivityEntry> extends NavigationControl {
   private CompositeEntryFilterUi<T> filters;

   protected final void setFilters(CompositeEntryFilterUi<T> var1) {
      Preconditions.checkNotNull(var1, "filters");
      this.filters = var1;
      var1.addFilterListener(new FilterListener() {
         @Override
         public void filterChanged(ActivityEntryFilterUi<?> var1) {
            FilteredNavigationControl.this.filtersChanged();
         }
      });
      this.customizeControls();
   }

   private void customizeControls() {
      final JPanel var1 = new JPanel() {
         @Override
         public Dimension getMaximumSize() {
            return this.getPreferredSize();
         }
      };
      InvisiblePanel.convert(var1);
      UiCanvas var2 = new UiCanvas() {
         @Override
         public void display(Component var1x) {
            var1.add(var1x);
         }
      };
      this.filters.decorateLauncher(var2);
      this.addControl(var1, 10);
      this.addSpace(10, 1);
   }

   public ActivityEntryFilterUi<T> getFilters() {
      return this.filters;
   }

   public final List<T> getFilteredList(List<T> var1) {
      return this.filters.getFilteredList(var1);
   }

   public abstract void filtersChanged();
}
