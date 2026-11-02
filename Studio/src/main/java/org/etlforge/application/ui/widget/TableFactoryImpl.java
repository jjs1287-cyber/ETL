package org.etlforge.application.ui.widget;

import javax.swing.table.JTableHeader;
import org.etlforge.application.ui.look.LookAndFeelCustomizer;
import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;

final class TableFactoryImpl implements TableFactory {
   @Override
   public KongaTable newTable(KongaRowTableModel var1) {
      return this.newTable(var1, -1);
   }

   @Override
   public KongaTable newTable(KongaRowTableModel var1, int var2) {
      KongaTable var3 = new KongaTable(var1);
      this.customizeTable(var3, var2);
      return var3;
   }

   @Override
   public KongaTable newSortedTable(KongaRowTableModel var1) {
      KongaTable var2 = this.newTable(var1);
      var2.setAutoCreateRowSorter(true);
      return var2;
   }

   @Override
   public KongaTable newSortedTable(KongaRowTableModel var1, int var2) {
      KongaTable var3 = this.newTable(var1, var2);
      var3.setAutoCreateRowSorter(true);
      return var3;
   }

   private void customizeTable(KongaTable var1, int var2) {
      var1.setShowGrid(false);
      LookAndFeelCustomizer.makeOver(var1);
      this.customizeTableHeader(var1);
      this.customizeRowHeight(var1);
      this.setVisibleRows(var1, var2);
   }

   private void customizeTableHeader(KongaTable var1) {
      JTableHeader var2 = var1.getTableHeader();
      var2.setReorderingAllowed(false);
   }

   private void customizeRowHeight(KongaTable var1) {
      int var2 = var1.getRowHeight();
      var2 += 4;
      var1.setRowHeight(var2);
   }

   public void setVisibleRows(KongaTable var1, int var2) {
      if (var2 > 0) {
         var1.setVisibleRowCount(var2);
      }
   }
}
