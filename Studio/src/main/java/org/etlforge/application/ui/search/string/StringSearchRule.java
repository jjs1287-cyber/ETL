package org.etlforge.application.ui.search.string;

import org.etlforge.string.StringMatcher;
import org.etlforge.string.StringMatchers;
import org.etlforge.string.WildcardStringMatcher;

public interface StringSearchRule {
   StringSearchRule STARTS_WITH = new DefaultStringSearchRule(StringMatchers.STARTS_WITH, Strings.get("StringSearch.SearchOption.Starts"));
   StringSearchRule STARTS_WITH_IGNORE_CASE = new DefaultStringSearchRule(
      StringMatchers.STARTS_WITH_IGNORE_CASE, Strings.get("StringSearch.SearchOption.Starts")
   );
   StringSearchRule CONTAINS = new DefaultStringSearchRule(StringMatchers.CONTAINS, Strings.get("StringSearch.SearchOption.Contains"));
   StringSearchRule CONTAINS_IGNORE_CASE = new DefaultStringSearchRule(StringMatchers.CONTAINS_IGNORE_CASE, Strings.get("StringSearch.SearchOption.Contains"));
   StringSearchRule ENDS_WITH = new DefaultStringSearchRule(StringMatchers.ENDS_WITH, Strings.get("StringSearch.SearchOption.Ends"));
   StringSearchRule ENDS_WITH_IGNORE_CASE = new DefaultStringSearchRule(StringMatchers.ENDS_WITH_IGNORE_CASE, Strings.get("StringSearch.SearchOption.Ends"));
   StringSearchRule WILDCARD = new DefaultStringSearchRule(
      new WildcardStringMatcher(), Strings.get("StringSearch.SearchOption.Wildcard"), Strings.get("StringSearch.SearchOption.Wildcard.Description")
   );
   StringSearchRule WILDCARD_IGNORE_CASE = new DefaultStringSearchRule(
      new WildcardStringMatcher(false), Strings.get("StringSearch.SearchOption.Wildcard"), Strings.get("StringSearch.SearchOption.Wildcard.Description")
   );

   String getName();

   String getDescription();

   StringMatcher getMatcher();
}
