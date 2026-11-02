package org.etlforge.integration.client.database.ui.primarykeys;

import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;

final class NoKeySelectedNode extends DefaultKongaTreeNode {
   public NoKeySelectedNode(TableNode var1) {
      super(var1, DatabaseUiResources.getString("UpdateKeys.NoKeys"), false);
   }
}
