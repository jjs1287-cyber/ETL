package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.etlforge.integration.database.info.DatabaseColumn;

class ColumnNode extends AbstractMutableTreeTableNode {
   public ColumnNode(DatabaseColumn var1) {
      super(var1, false);
   }

   @Override
   public int getColumnCount() {
      return 3;
   }

   @Override
   public Object getValueAt(int var1) {
      DatabaseColumn var2 = (DatabaseColumn)this.getUserObject();
      switch (var1) {
         case 0:
            return var2;
         case 1:
            return var2.getDataTypeString();
         case 2:
            return var2.isPrimaryKey();
         default:
            throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }
}
