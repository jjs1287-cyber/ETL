package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.WildcardStringMatcher;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilterFunction;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;

final class TreeFilters {
   private static final StringMatcher MATCHER = new WildcardStringMatcher(false);
   private static final String DESCRIPTION = "* = any string, ? = any character";

   public static void installFilter(TreePanel var0) {
      var0.showFilterField(createFilterFunction());
      var0.setFilterFieldDescription("* = any string, ? = any character");
   }

   private static LeafDisplayNameFilterFunction createFilterFunction() {
      return new LeafDisplayNameFilterFunction(MATCHER, true);
   }

   public static SimpleTreeFilterField createSearchField(KongaTree var0) {
      LeafDisplayNameFilterFunction var1 = createFilterFunction();
      SimpleTreeFilterField var2 = new SimpleTreeFilterField(var0, var1);
      var2.setFieldWidth(20);
      var2.setDescription("* = any string, ? = any character");
      return var2;
   }
}
