package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.etlforge.integration.database.info.DatabaseObject;

class DatabaseTableNode extends AbstractMutableTreeTableNode {
   public DatabaseTableNode(DatabaseObject var1) {
      super(var1, true);
   }

   @Override
   public boolean isLeaf() {
      return false;
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   @Override
   public Object getValueAt(int var1) {
      if (var1 == 0) {
         return this.getUserObject();
      } else {
         throw new IndexOutOfBoundsException("index: " + var1);
      }
   }
}
