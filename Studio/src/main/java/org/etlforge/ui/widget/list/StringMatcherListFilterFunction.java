package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import javax.annotation.Nullable;
import org.etlforge.string.StringMatcher;

public final class StringMatcherListFilterFunction implements Function<String, ListFilter> {
   private final StringMatcher stringMatcher;
   private final Function<Object, String> stringProducer;

   public static StringMatcherListFilterFunction of(StringMatcher var0, Function<Object, String> var1) {
      return new StringMatcherListFilterFunction(var0, var1);
   }

   public StringMatcherListFilterFunction(StringMatcher var1, Function<Object, String> var2) {
      this.stringMatcher = Preconditions.checkNotNull(var1, "stringMatcher");
      this.stringProducer = Preconditions.checkNotNull(var2, "stringProducer");
   }

   @Nullable
   public ListFilter apply(@Nullable String var1) {
      return Strings.isNullOrEmpty(var1) ? null : new StringMatcherListFilterFunction.FilterImpl(var1);
   }

   private class FilterImpl implements ListFilter {
      private final String filterText;

      public FilterImpl(String var2) {
         this.filterText = var2;
      }

      @Override
      public boolean test(Object var1) {
         String var2 = StringMatcherListFilterFunction.this.stringProducer.apply(var1);
         return var2 == null ? true : StringMatcherListFilterFunction.this.stringMatcher.matches(this.filterText, var2);
      }
   }
}
