package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public final class NodesWithDataVisibiltyFilter implements NodeVisibilityFilter {
   private final TreeComponent tree;

   public NodesWithDataVisibiltyFilter(TreeComponent var1) {
      this.tree = var1;
   }

   @Override
   public boolean isHidden(Node var1) {
      return this.tree.hasData() && var1.isEmpty();
   }
}
