package org.etlforge.integration.uidelegate.entity.tree;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public final class TopLevelCategoryTreeNode extends DefaultKongaTreeNode {
   private Icon icon = EntityIcons.forType(EntityType.Folder).getDefaultIcon(EntityIcons.Size.SMALL);
   private final List<IntegrationEntityTreeNode> rootFolderNodes = Lists.newArrayList();

   public TopLevelCategoryTreeNode(KongaTreeNode var1, Object var2) {
      super(var1, var2, true);
   }

   public TopLevelCategoryTreeNode(Object var1) {
      super(var1, true);
   }

   @Override
   public void addChild(KongaTreeNode var1) {
      super.addChild(var1);
      if (isRootFolderNode(var1)) {
         this.rootFolderNodes.add((IntegrationEntityTreeNode)var1);
      }
   }

   private static boolean isRootFolderNode(KongaTreeNode var0) {
      return var0 instanceof IntegrationEntityTreeNode && var0.getUserObject() instanceof RootFolder;
   }

   @Override
   public void remove(int var1) {
      KongaTreeNode var2 = this.getChildAt(var1);
      if (isRootFolderNode(var2)) {
         this.rootFolderNodes.remove(var2);
      }

      super.remove(var1);
   }

   public void setNodeIcon(Icon var1) {
      this.icon = var1;
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return this.icon;
   }

   @Override
   public boolean sortChildren() {
      return false;
   }

   public Iterable<IntegrationEntityTreeNode> collectRootFolderNodes() {
      return Collections.unmodifiableList(this.rootFolderNodes);
   }
}
