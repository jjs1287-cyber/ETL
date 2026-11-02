package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;

public class DefaultTableRowOrderControl implements TableRowOrderControl {
   private final AbstractKongaTableModel model;

   public DefaultTableRowOrderControl(AbstractKongaTableModel var1) {
      this.model = Preconditions.checkNotNull(var1);
   }

   @Override
   public boolean canRowMoveUp(int var1) {
      return var1 > 0 && var1 < this.model.getRowCount();
   }

   @Override
   public void moveRowUp(int var1) {
      if (this.canRowMoveUp(var1)) {
         this.model.startCompoundEdit();
         Object var2 = this.model.getRowObjectAt(var1);
         this.model.deleteRow(var1);
         this.model.addRowAt(var1 - 1, var2);
         this.model.stopCompoundEdit();
      }
   }

   @Override
   public boolean canRowMoveDown(int var1) {
      return var1 >= 0 && var1 < this.model.getRowCount() - 1;
   }

   @Override
   public void moveRowDown(int var1) {
      if (this.canRowMoveDown(var1)) {
         this.model.startCompoundEdit();
         Object var2 = this.model.getRowObjectAt(var1);
         this.model.deleteRow(var1);
         this.model.addRowAt(var1 + 1, var2);
         this.model.stopCompoundEdit();
      }
   }
}
