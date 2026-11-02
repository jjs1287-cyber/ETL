package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public interface NodeVisibilityFilter {
   NodeVisibilityFilter NOTHING_HIDDEN = new NodeVisibilityFilter() {
      @Override
      public boolean isHidden(Node var1) {
         return false;
      }
   };

   boolean isHidden(Node var1);
}
