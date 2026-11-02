package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public enum ElementCardinality {
   ZERO_OR_ONE,
   ONCE_ONLY,
   ZERO_OR_MORE,
   ONE_OR_MORE;

   public static ElementCardinality fromTreeNode(Node var0) {
      boolean var1 = var0.isAtLeastOne();
      if (var0.isLoopNode()) {
         return var1 ? ONE_OR_MORE : ZERO_OR_MORE;
      } else {
         return var1 ? ONCE_ONLY : ZERO_OR_ONE;
      }
   }
}
