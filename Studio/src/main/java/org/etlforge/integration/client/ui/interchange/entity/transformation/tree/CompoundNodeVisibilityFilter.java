package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public final class CompoundNodeVisibilityFilter implements NodeVisibilityFilter {
   private final List<NodeVisibilityFilter> filters;

   public CompoundNodeVisibilityFilter(NodeVisibilityFilter... var1) {
      this.filters = Lists.newArrayList(var1);
   }

   public boolean isEmpty() {
      return this.filters.isEmpty();
   }

   public void addFilter(NodeVisibilityFilter var1) {
      this.filters.add(var1);
   }

   @Override
   public boolean isHidden(Node var1) {
      for (NodeVisibilityFilter var3 : this.filters) {
         if (var3.isHidden(var1)) {
            return true;
         }
      }

      return false;
   }
}
