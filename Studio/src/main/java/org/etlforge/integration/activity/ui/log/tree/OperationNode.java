package org.etlforge.integration.activity.ui.log.tree;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;

final class OperationNode extends AbstractMutableTreeTableNode {
   private final OperationLogTableColumn[] columnDescriptors;
   private boolean selected;

   public OperationNode(OperationLogEntry var1, OperationLogTableColumn[] var2) {
      super(var1, true);
      this.columnDescriptors = var2;
   }

   public OperationLogEntry getUserObject() {
      return (OperationLogEntry)super.getUserObject();
   }

   @Override
   public Object getValueAt(int var1) {
      OperationLogTableColumn var2 = this.columnDescriptors[var1];
      if (var2 == OperationLogTableColumn.SELECT_CHECK) {
         return this.selected;
      }

      OperationLogEntry var3 = this.getUserObject();
      return var2.createColumnValue(var3);
   }

   @Override
   public void setValueAt(Object var1, int var2) {
      if (this.columnDescriptors[var2] == OperationLogTableColumn.SELECT_CHECK) {
         this.selected = (Boolean)var1;
      }

      super.setValueAt(var1, var2);
   }

   @Override
   public int getColumnCount() {
      return this.columnDescriptors.length;
   }

   @Override
   public boolean isEditable(int var1) {
      return this.columnDescriptors[var1].isEditable();
   }
}
