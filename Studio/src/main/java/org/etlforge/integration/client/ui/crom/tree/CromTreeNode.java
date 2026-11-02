package org.etlforge.integration.client.ui.crom.tree;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public final class CromTreeNode extends DefaultKongaTreeNode {
   public CromTreeNode(KongaTreeNode var1, CROM var2) {
      super(var1, var2, canHaveChildren(var2));
   }

   private static boolean canHaveChildren(CROM var0) {
      return !var0.isValueLeaf() && !var0.isAttributeNode();
   }

   public CROM getUserObject() {
      return (CROM)super.getUserObject();
   }

   @Override
   public boolean isLeaf() {
      return !canHaveChildren(this.getUserObject());
   }

   public boolean isFullyExpanded() {
      return this.getUserObject().hasMoreThanNilChildren();
   }

   @Override
   public String getDisplayName() {
      return this.getUserObject().getDisplayText();
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      if (this.getUserObject().isLeaf()) {
         return null;
      } else {
         return var1 == NodeStatus.OPENED ? CommonIcons.FOLDER_OPEN_16 : CommonIcons.FOLDER_CLOSED_16;
      }
   }
}
