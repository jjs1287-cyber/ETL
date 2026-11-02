package org.etlforge.integration.client.ui.project.info;

import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeCompositeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter2;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.MemoizingKongaTreeFilter;

public abstract class AbstractTreeFilterControl implements TreeFilterControl {
   private KongaTreeFilter textFilter;
   private KongaTree tree;
   private KongaTreeFilter nonTextFilter;

   protected final void setNonTextFilter(KongaTreeFilter var1) {
      this.nonTextFilter = var1;
      this.update();
   }

   @Override
   public final void setTree(TreePanel var1) {
      this.tree = var1.getTree();
      var1.setFilterInterceptor(new Receiver<KongaTreeFilter>() {
         public void handle(KongaTreeFilter var1) {
            AbstractTreeFilterControl.this.textFilter = var1;
            AbstractTreeFilterControl.this.update();
         }
      });
   }

   @Override
   public final void update() {
      if (this.tree != null) {
         this.tree.setFilter(this.getCombinedFilter());
      }
   }

   private KongaTreeFilter getCombinedFilter() {
      if (this.textFilter == null) {
         return this.nonTextFilter;
      } else {
         return this.nonTextFilter == null
            ? this.textFilter
            : new MemoizingKongaTreeFilter(new AbstractTreeFilterControl.CompoundFilter(this.nonTextFilter, this.textFilter));
      }
   }

   private static class CompoundFilter implements KongaTreeFilter2 {
      private final KongaTreeFilter impl;

      public CompoundFilter(KongaTreeFilter... var1) {
         this.impl = new KongaTreeCompositeFilter(var1);
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
