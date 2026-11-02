package org.etlforge.integration.client.ui.de.global.tree;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.de.resources.DataElementResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

final class ETLForgeVariablesNode extends DefaultKongaTreeNode {
   public ETLForgeVariablesNode(KongaTreeNode var1) {
      super(var1, DataElementResources.getString("DataElement.Tree.ETLForge.Name"), true);
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ClientIcons.APPLICATION_16;
   }
}
