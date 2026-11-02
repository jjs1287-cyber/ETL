package org.etlforge.integration.client.ui.entity.search;

import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.util.preference.KongaPreferences;

class SearchRules {
   private static final StringSearchRule[] RULES = new StringSearchRule[]{StringSearchRule.CONTAINS_IGNORE_CASE, StringSearchRule.WILDCARD_IGNORE_CASE};
   private static final String PREFERENCE_KEY = "SearchRule";
   private final KongaPreferences preferences = KongaPreferences.getPreferences(SearchRules.class);

   public StringSearchRule[] getRules() {
      return (StringSearchRule[])RULES.clone();
   }

   public StringSearchRule getDefaultRule() {
      int var1 = this.preferences.getInt("SearchRule", 0);
      return RULES[var1];
   }

   public void rememberSelectedRule(StringSearchRule var1) {
      int var2 = this.getIndexToStore(var1);
      this.preferences.putInt("SearchRule", var2);
   }

   private int getIndexToStore(StringSearchRule var1) {
      for (int var2 = 0; var2 < RULES.length; var2++) {
         if (var1.equals(RULES[var2])) {
            return var2;
         }
      }

      return 0;
   }
}
