package org.etlforge.ui.text.contentassist;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Comparator;

final class DisplayValueOrder implements Comparator<CompletionToken> {
   public static final Comparator<CompletionToken> CASE_SENSITIVE = new DisplayValueOrder(false);
   public static final Comparator<CompletionToken> CASE_INSENSITIVE = new DisplayValueOrder(true);
   private final Ordering<CompletionToken> impl;

   private DisplayValueOrder(boolean var1) {
      Ordering var2 = var1 ? Ordering.from(String.CASE_INSENSITIVE_ORDER) : Ordering.natural();
      this.impl = var2.onResultOf(new DisplayValueOrder.ValueFunction());
   }

   public int compare(CompletionToken var1, CompletionToken var2) {
      return this.impl.compare(var1, var2);
   }

   private static class ValueFunction implements Function<CompletionToken, String>, Serializable {
      private ValueFunction() {
      }

      public String apply(CompletionToken var1) {
         return var1.getDisplayValue();
      }
   }
}
