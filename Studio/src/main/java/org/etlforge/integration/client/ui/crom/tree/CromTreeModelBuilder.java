package org.etlforge.integration.client.ui.crom.tree;

import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;

final class CromTreeModelBuilder {
   private boolean includeLeafNodes;

   public final void setIncludeLeafNodes(boolean var1) {
      this.includeLeafNodes = var1;
   }

   public DefaultKongaTreeModel buildModel(CROM var1) {
      CromTreeNode var2 = new CromTreeNode(null, var1);
      this.buildTree(var1, var2);
      return new DefaultKongaTreeModel(var2);
   }

   public void buildTree(CROM var1, CromTreeNode var2) {
      for (CROM var4 : var1.getChildren()) {
         if (this.isIncluded(var4)) {
            var2.setAllowsChildren(true);
            CromTreeNode var5 = new CromTreeNode(var2, var4);
            this.buildTree(var4, var5);
         }
      }
   }

   private boolean isIncluded(CROM var1) {
      if (var1.isAttributeValue()) {
         return false;
      } else {
         return var1.isLeaf() ? this.includeLeafNodes : true;
      }
   }
}
