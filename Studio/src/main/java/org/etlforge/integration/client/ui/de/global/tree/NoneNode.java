package org.etlforge.integration.client.ui.de.global.tree;

import org.etlforge.integration.client.ui.de.resources.DataElementResources;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class NoneNode extends DefaultKongaTreeNode {
   public NoneNode(KongaTreeNode var1) {
      super(var1, DataElementResources.getString("DataElement.Tree.None"), false);
   }
}
