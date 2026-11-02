package org.etlforge.string;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;

public final class StringMatcherPredicate implements Predicate<String>, Serializable {
   private final StringMatcher matcher;
   private final String searchString;

   public StringMatcherPredicate(StringMatcher var1, String var2) {
      this.matcher = Preconditions.checkNotNull(var1, "matcher");
      this.searchString = var2;
   }

   public boolean apply(String var1) {
      return this.matcher.matches(this.searchString, var1);
   }
}
