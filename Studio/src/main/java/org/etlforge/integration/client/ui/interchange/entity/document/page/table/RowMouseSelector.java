package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

final class RowMouseSelector extends MouseAdapter implements ListSelectionListener {
   private final DocDefTableUi ui;
   private final Set<Integer> selectedRows;

   RowMouseSelector(DocDefTableUi var1) {
      this.ui = var1;
      this.selectedRows = new HashSet<>();
   }

   private void handleCtrlClickOnFirstColumn(int var1) {
      if (this.selectedRows.contains(var1)) {
         this.selectedRows.remove(var1);
      } else {
         this.selectedRows.add(var1);
      }
   }

   private void handleClickOnFirstColumn(int var1) {
      this.selectedRows.clear();
      this.selectedRows.add(var1);
   }

   private void handleClickOnColumnOtherThanFirst() {
      this.clear();
   }

   @Override
   public void mouseClicked(MouseEvent var1) {
      Point var2 = var1.getPoint();
      int var3 = this.ui.rowAtPoint(var2);
      int var4 = this.ui.columnAtPoint(var2);
      if (var3 >= 0 && var4 >= 0) {
         if (var4 == 0) {
            boolean var5 = (var1.getModifiersEx() & 128) != 0;
            if (var5) {
               this.handleCtrlClickOnFirstColumn(var3);
            } else {
               this.handleClickOnFirstColumn(var3);
            }
         } else {
            this.handleClickOnColumnOtherThanFirst();
         }

         this.updateSelection(var3, var4);
      }
   }

   private void updateSelection(final int var1, final int var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (var2 > 0) {
               RowMouseSelector.this.ui.changeSelection(var1, var2, false, false);
            }

            RowMouseSelector.this.ui.repaint();
         }
      });
   }

   public boolean isRowSelected(int var1) {
      return this.selectedRows.contains(var1);
   }

   void clear() {
      this.selectedRows.clear();
   }

   @Override
   public void valueChanged(ListSelectionEvent var1) {
      if (!var1.getValueIsAdjusting()) {
         if (this.ui.getSelectedColumn() > 0) {
            this.clear();
         }
      }
   }
}
