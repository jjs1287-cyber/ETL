package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public final class MappedTargetNodeVisibilityFilter implements NodeVisibilityFilter {
   @Override
   public boolean isHidden(Node var1) {
      return !this.isMapped(var1);
   }

   private boolean isMapped(Node var1) {
      if (var1.isFolder()) {
         for (Node var3 : var1.getChildren()) {
            if (this.isMapped(var3)) {
               return true;
            }
         }

         return false;
      } else {
         return var1.isTargetMapped();
      }
   }
}
