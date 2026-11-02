package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.Selection;

public class DefaultDeleteRowsAction extends AbstractSelectionAction {
   private KongaRowTable table;

   public DefaultDeleteRowsAction(KongaRowTable var1) {
      this.setTable(var1);
   }

   public DefaultDeleteRowsAction(KongaRowTable var1, ActionDecorator var2) {
      var2.decorate(this);
      this.setTable(var1);
   }

   public final void setTable(KongaRowTable var1) {
      this.table = Preconditions.checkNotNull(var1);
      this.setSelectionSource(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      int[] var2 = this.getSelection().indeces();
      KongaRowTableModel var3 = this.table.getModel();

      for (int var4 = var2.length - 1; var4 >= 0; var4--) {
         int var5 = var2[var4];
         if (this.canRowBeDeleted(var5)) {
            var3.deleteRow(var5);
         }
      }
   }

   public final IndexedSelection getSelection() {
      Selection var1 = super.getSelection();
      return var1 == null ? IndexedSelection.EMPTY : (IndexedSelection)var1;
   }

   @Override
   protected final void handleSelectionChange() {
      IndexedSelection var1 = this.getSelection();
      if (var1.isEmpty()) {
         this.setEnabled(false);
      } else {
         this.setEnabled(this.areAllSelectedRowsDeletable(var1));
      }
   }

   protected final KongaRowTable getTable() {
      return this.table;
   }

   private boolean areAllSelectedRowsDeletable(IndexedSelection var1) {
      for (int var5 : var1.indeces()) {
         if (!this.canRowBeDeleted(var5)) {
            return false;
         }
      }

      return true;
   }

   protected boolean canRowBeDeleted(int var1) {
      return true;
   }
}
