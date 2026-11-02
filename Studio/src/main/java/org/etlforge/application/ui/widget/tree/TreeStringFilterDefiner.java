package org.etlforge.application.ui.widget.tree;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.search.string.AbstractStringSearchService;
import org.etlforge.application.ui.search.string.StringSearchModel;
import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class TreeStringFilterDefiner extends AbstractStringSearchService {
   private final KongaTree tree;

   public TreeStringFilterDefiner(KongaTree var1) {
      Preconditions.checkNotNull(var1, "tree");
      this.tree = var1;
   }

   public TreeStringFilterDefiner(KongaTree var1, StringSearchRule... var2) {
      super(var2);
      Preconditions.checkNotNull(var1, "tree");
      this.tree = var1;
   }

   @Override
   protected void applyNewFilter() {
      this.tree.setFilter(new TreeStringFilterDefiner.FilterImpl());
   }

   public boolean isSubjectToFiltering(KongaTreeNode var1) {
      return true;
   }

   public boolean filter(KongaTreeNode var1, StringSearchModel var2) {
      return var2.test(var1.getDisplayName());
   }

   private class FilterImpl implements KongaTreeFilter {
      private FilterImpl() {
      }

      @Override
      public boolean apply(KongaTreeNode var1) {
         return TreeStringFilterDefiner.this.isSubjectToFiltering(var1)
            ? TreeStringFilterDefiner.this.filter(var1, TreeStringFilterDefiner.this.getSearchModel())
            : true;
      }
   }
}
