package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Function;
import javax.swing.tree.TreeNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeObjectConverter;

public final class NodeToEntity implements Function<KongaTreeNode, IntegrationEntity>, NodeObjectConverter {
   public IntegrationEntity apply(KongaTreeNode var1) {
      Object var2 = var1.getUserObject();
      return var2 instanceof IntegrationEntity ? (IntegrationEntity)var2 : null;
   }

   @Override
   public TreeNode toNode(Object var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public Object toObject(TreeNode var1) {
      return var1 instanceof KongaTreeNode ? this.apply((KongaTreeNode)var1) : null;
   }
}
