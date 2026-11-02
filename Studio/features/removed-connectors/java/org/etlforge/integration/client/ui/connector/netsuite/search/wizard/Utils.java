package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Function;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilterFunction;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilterFunctionCheck;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;

final class Utils {
   public static KongaTree createEmptyTree() {
      KongaTree var0 = new KongaTree();
      var0.setRootVisible(false);
      return var0;
   }

   public static SimpleTreeFilterField createFilterField(KongaTree var0) {
      return createFilterField(var0, new LeafDisplayNameFilterFunction(StringMatchers.CONTAINS_IGNORE_CASE, true));
   }

   public static SimpleTreeFilterField createCheckBoxFilterField(KongaTree var0) {
      LeafDisplayNameFilterFunction var1 = new LeafDisplayNameFilterFunction(StringMatchers.CONTAINS_IGNORE_CASE, true);
      return createFilterField(var0, new LeafDisplayNameFilterFunctionCheck(var1));
   }

   public static SimpleTreeFilterField createFilterField(KongaTree var0, Function<String, KongaTreeFilter> var1) {
      SimpleTreeFilterField var2 = new SimpleTreeFilterField(var0, var1);
      var2.setLabel("Filter:");
      return var2;
   }

   private Utils() {
   }
}
