package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.etlforge.ui.util.progress.ProgressMessage;

class DownloadingIndicatorNode extends AbstractMutableTreeTableNode {
   private final ProgressMessage text;

   public DownloadingIndicatorNode(String var1) {
      this.text = new ProgressMessage(var1);
      this.setUserObject(var1);
   }

   @Override
   public boolean isLeaf() {
      return true;
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   @Override
   public Object getValueAt(int var1) {
      if (var1 == 0) {
         return this.text;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }

   public void update() {
      this.text.update();
   }

   @Override
   public String toString() {
      return this.text.getText();
   }
}
