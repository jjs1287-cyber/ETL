package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Function;
import org.etlforge.integration.client.ui.entity.tree.EntityTreeFilter;
import org.etlforge.integration.client.ui.entity.tree.ProjectPathFilterFunction;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeCompositeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter2;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class SingleTreeFilterFunction implements Function<String, KongaTreeFilter> {
   private final ProjectPathFilterFunction pathFilter;
   private ContentViewerProcessFilter processFilter;
   private String filterString;

   public SingleTreeFilterFunction(IntegrationProject var1) {
      this.pathFilter = new ProjectPathFilterFunction(var1);
      this.filterString = null;
   }

   public void setProcessFilter(ContentViewerProcessFilter var1) {
      this.processFilter = var1;
   }

   public KongaTreeFilter removePathFilter() {
      return this.apply((String)null);
   }

   public KongaTreeFilter apply(String var1) {
      this.filterString = var1;
      return this.getCurrentFilter();
   }

   public KongaTreeFilter getCurrentFilter() {
      KongaTreeFilter var1 = this.filterString == null ? null : this.pathFilter.apply(this.filterString);
      KongaTreeFilter var2 = this.processFilter == null ? null : EntityTreeFilter.memoize(this.processFilter);
      if (var1 != null && var2 != null) {
         return new SingleTreeFilterFunction.CombinedFilter(var1, var2);
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private static class CombinedFilter implements KongaTreeFilter2 {
      private final KongaTreeCompositeFilter impl;

      public CombinedFilter(KongaTreeFilter var1, KongaTreeFilter var2) {
         assert var1 != null || var2 != null;
         this.impl = new KongaTreeCompositeFilter();
         if (var2 != null) {
            this.impl.addFilter(var2);
         }

         if (var1 != null) {
            this.impl.addFilter(var1);
         }
      }

      @Override
      public boolean apply(KongaTreeNode var1) {
         return this.impl.apply(var1);
      }

      @Override
      public void hasBeenApplied(KongaTree var1) {
         var1.expandAllNonEmptyFolderNodes();
      }
   }
}
