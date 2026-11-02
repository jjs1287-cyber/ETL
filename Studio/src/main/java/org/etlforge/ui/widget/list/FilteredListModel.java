package org.etlforge.ui.widget.list;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class FilteredListModel extends AbstractListModel implements ListModel {
   private final ListModel unfiltered;
   private final List<ListFilter> filters;
   private int filteredSize;
   private int[] filteredIndeces;

   public FilteredListModel(ListModel var1) {
      this.unfiltered = var1;
      var1.addListDataListener(new FilteredListModel.UnfilteredModelListener());
      this.filters = new ArrayList<>();
      this.calculateFilteredIndeces();
   }

   public final void addFilter(ListFilter var1) {
      this.filters.add(var1);
      this.update();
   }

   public final void removeFilter(ListFilter var1) {
      this.filters.remove(var1);
      this.update();
   }

   public final void setFilter(ListFilter var1) {
      this.filters.clear();
      if (var1 != null) {
         this.filters.add(var1);
      }

      this.update();
   }

   public final List<ListFilter> getFilters() {
      return Lists.newArrayList(this.filters);
   }

   public ListModel getUnfilteredModel() {
      return this.unfiltered;
   }

   @Override
   public final Object getElementAt(int var1) {
      return this.unfiltered.getElementAt(this.filteredIndeces[var1]);
   }

   @Override
   public final int getSize() {
      return this.filteredSize;
   }

   private void update() {
      this.calculateFilteredIndeces();
      this.fireContentsChanged(this, -1, -1);
   }

   private void calculateFilteredIndeces() {
      int var1 = this.unfiltered.getSize();
      this.filteredIndeces = new int[var1];
      if (this.filters.isEmpty()) {
         this.noFiltering();
      } else {
         this.filteredSize = 0;

         for (int var2 = 0; var2 < var1; var2++) {
            Object var3 = this.unfiltered.getElementAt(var2);
            boolean var4 = true;

            for (ListFilter var6 : this.filters) {
               if (!var6.test(var3)) {
                  var4 = false;
                  break;
               }
            }

            if (var4) {
               this.filteredIndeces[this.filteredSize++] = var2;
            }
         }
      }
   }

   private void noFiltering() {
      this.filteredSize = this.unfiltered.getSize();
      int var1 = 0;

      while (var1 < this.filteredSize) {
         this.filteredIndeces[var1] = var1++;
      }
   }

   private class UnfilteredModelListener implements ListDataListener {
      private UnfilteredModelListener() {
      }

      @Override
      public void contentsChanged(ListDataEvent var1) {
         FilteredListModel.this.update();
      }

      @Override
      public void intervalAdded(ListDataEvent var1) {
         FilteredListModel.this.update();
      }

      @Override
      public void intervalRemoved(ListDataEvent var1) {
         FilteredListModel.this.update();
      }
   }
}
