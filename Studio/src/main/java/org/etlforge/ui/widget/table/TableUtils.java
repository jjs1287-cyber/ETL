package org.etlforge.ui.widget.table;

import com.google.common.collect.Lists;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionFactory;

public final class TableUtils {
   public static JScrollPane wrapInScrollPreserveWidth(final JTable var0) {
      return new JScrollPane(var0) {
         @Override
         public Dimension getPreferredSize() {
            return new Dimension(var0.getPreferredSize().width, super.getPreferredSize().height);
         }

         @Override
         public Dimension getMinimumSize() {
            return this.getPreferredSize();
         }
      };
   }

   public static void setVisibleRowCount(JTable var0, int var1) {
      ArgCheck.checkPositive(var1, "visibleRows");
      Dimension var2 = var0.getPreferredScrollableViewportSize();
      var2.height = var0.getRowHeight() * var1;
      var0.setPreferredScrollableViewportSize(var2);
   }

   public static OrderedSelection toSelection(AbstractKongaTableModel var0, int[] var1) {
      ArrayList var2 = Lists.newArrayList();

      for (int var6 : var1) {
         var2.add(var0.getRowObjectAt(var6));
      }

      return var2.isEmpty() ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var2);
   }

   private TableUtils() {
   }
}
