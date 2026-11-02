package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.json;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeVisibilityFilter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.crom.CROM;

public final class JsonNodeVisibilityFilter implements NodeVisibilityFilter {
   @Override
   public boolean isHidden(Node var1) {
      CROM var2 = var1.getCROM();
      return !var2.getName().equals("/PRECONDITION/") && !var2.getName().equals("/PRESCRIPT/") ? var2.isAttributeNode() : false;
   }
}
