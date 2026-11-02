package org.etlforge.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.io.LineProcessor;
import java.io.IOException;

public abstract class FilteredLineProcessor<T> implements LineProcessor<T> {
   public static final Predicate<String> HASHMARK_COMMENT = new Predicate<String>() {
      public boolean apply(String var1) {
         return !var1.isEmpty() && !var1.startsWith("#");
      }
   };
   private final Predicate<String> filter;
   private final boolean trimLines;

   public FilteredLineProcessor(Predicate<String> var1, boolean var2) {
      this.filter = Preconditions.checkNotNull(var1, "filter");
      this.trimLines = var2;
   }

   @Override
   public final boolean processLine(String var1) throws IOException {
      String var2 = this.trimLines ? var1.trim() : var1;
      return this.filter.apply(var2) ? this.handleAcceptedLine(var2) : this.handleRejectedLine(var2);
   }

   protected abstract boolean handleAcceptedLine(String var1) throws IOException;

   protected boolean handleRejectedLine(String var1) throws IOException {
      return true;
   }
}
