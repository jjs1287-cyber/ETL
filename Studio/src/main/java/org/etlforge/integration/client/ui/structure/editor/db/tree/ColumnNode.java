package org.etlforge.integration.client.ui.structure.editor.db.tree;

import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;

public class ColumnNode extends DefaultKongaTreeNode {
   public ColumnNode(TableNode var1, DatabaseColumn var2) {
      super(var1, var2, false);
      if (var1 == null) {
         throw new IllegalArgumentException("parent can't be null.");
      }

      if (var2 == null) {
         throw new IllegalArgumentException("column can't be null.");
      }
   }

   public DatabaseColumn getUserObject() {
      return (DatabaseColumn)super.getUserObject();
   }
}
