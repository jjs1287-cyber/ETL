package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public final class MappedSourceNodeVisibilityFilter implements NodeVisibilityFilter {
   @Override
   public boolean isHidden(Node var1) {
      return !this.isUsed(var1);
   }

   private boolean isUsed(Node var1) {
      if (var1.isFolder()) {
         for (Node var3 : var1.getChildren()) {
            if (this.isUsed(var3)) {
               return true;
            }
         }

         return false;
      } else {
         return var1.isSourceUsed();
      }
   }
}
