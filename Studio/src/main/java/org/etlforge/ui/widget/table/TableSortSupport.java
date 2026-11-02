package org.etlforge.ui.widget.table;

import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultRowSorter;
import javax.swing.RowSorter;
import javax.swing.table.TableModel;

final class TableSortSupport {
   private final Map<Integer, Comparator<?>> indexCmps = Maps.newHashMap();
   private final Map<TableColumnMetaData, Comparator<?>> descriptorCmps = Maps.newHashMap();
   private boolean sortsOnUpdates;

   public boolean getSortsOnUpdates() {
      return this.sortsOnUpdates;
   }

   public void setSortsOnUpdates(boolean var1) {
      this.sortsOnUpdates = var1;
   }

   public void setComparator(int var1, Comparator<?> var2) {
      if (var2 == null) {
         this.indexCmps.remove(var1);
      } else {
         this.indexCmps.put(var1, var2);
      }
   }

   public void setComparator(TableColumnMetaData var1, Comparator<?> var2) {
      if (var2 == null) {
         this.descriptorCmps.remove(var1);
      } else {
         this.descriptorCmps.put(var1, var2);
      }
   }

   public void apply(KongaTable var1, RowSorter<? extends TableModel> var2) {
      if (var2 instanceof DefaultRowSorter) {
         this.applyImpl(var1, (DefaultRowSorter<?, ?>)var2);
      }
   }

   private void applyImpl(KongaTable var1, DefaultRowSorter<?, ?> var2) {
      var2.setSortsOnUpdates(this.sortsOnUpdates);
      this.applyIndexComparators(var2);
      this.applyColumnDescriptorComparators(var1, var2);
   }

   private void applyIndexComparators(DefaultRowSorter<?, ?> var1) {
      for (Entry var3 : this.indexCmps.entrySet()) {
         var1.setComparator((Integer)var3.getKey(), (Comparator<?>)var3.getValue());
      }
   }

   private void applyColumnDescriptorComparators(KongaTable var1, DefaultRowSorter<?, ?> var2) {
      if (!this.descriptorCmps.isEmpty()) {
         TableModel var3 = var1.getModel();
         if (var3 instanceof KongaTableModel) {
            KongaTableModel var4 = (KongaTableModel)var3;

            for (Entry var6 : this.descriptorCmps.entrySet()) {
               var2.setComparator(var4.indexOfColumn((TableColumnMetaData)var6.getKey()), (Comparator<?>)var6.getValue());
            }
         }
      }
   }
}
