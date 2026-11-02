package org.etlforge.integration.client.database.ui.primarykeys;

import javax.swing.Icon;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

final class KeyNode extends DefaultKongaTreeNode {
   public KeyNode(TableNode var1, DatabaseColumn var2) {
      super(var1, var2, false);
   }

   @Override
   public String getDisplayName() {
      return this.getUserObject().getName();
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return DatabaseUiResources.PRIMARY_KEY_ICON;
   }

   public DatabaseColumn getUserObject() {
      return (DatabaseColumn)super.getUserObject();
   }
}
