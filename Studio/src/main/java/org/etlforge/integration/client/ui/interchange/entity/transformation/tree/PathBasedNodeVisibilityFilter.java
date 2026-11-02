package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;

public final class PathBasedNodeVisibilityFilter implements NodeVisibilityFilter {
   private final TreeMapper treeMapper;
   private final MappingTreeNodePathFilter pathFilter;

   public PathBasedNodeVisibilityFilter(TreeMapper var1, MappingTreeNodePathFilter var2) {
      this.treeMapper = var1;
      this.pathFilter = var2;
   }

   @Override
   public boolean isHidden(Node var1) {
      return this.isSimpleMode() && this.pathFilter.isHidden(var1.m_deName);
   }

   private boolean isSimpleMode() {
      return this.treeMapper.getMode() == TreeMapperMode.SIMPLE;
   }
}
