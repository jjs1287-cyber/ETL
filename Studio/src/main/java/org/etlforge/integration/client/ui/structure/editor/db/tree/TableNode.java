package org.etlforge.integration.client.ui.structure.editor.db.tree;

import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;

public class TableNode extends DefaultKongaTreeNode {
   public TableNode(TableNode var1, DatabaseTable var2) {
      super(var1, var2, true);
   }

   public DatabaseTable getUserObject() {
      return (DatabaseTable)super.getUserObject();
   }
}
