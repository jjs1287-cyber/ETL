package com.etlforge.integration.client.ui.sfdc.soql.browser;

import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class RelationshipFilter implements KongaTreeFilter {
   private final boolean includeParents;
   private final boolean includeChildren;

   public RelationshipFilter(boolean var1, boolean var2) {
      this.includeParents = var1;
      this.includeChildren = var2;
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      return !this.includeChildren && var1 instanceof ChildRelationshipNode ? false : this.includeParents || !(var1 instanceof ParentRelationshipNode);
   }
}
