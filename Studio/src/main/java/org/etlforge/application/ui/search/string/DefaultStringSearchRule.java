package org.etlforge.application.ui.search.string;

import com.google.common.base.Preconditions;
import org.etlforge.string.StringMatcher;

public final class DefaultStringSearchRule implements StringSearchRule {
   private final StringMatcher matcher;
   private final String name;
   private final String description;

   public DefaultStringSearchRule(StringMatcher var1, String var2) {
      this(var1, var2, "");
   }

   public DefaultStringSearchRule(StringMatcher var1, String var2, String var3) {
      Preconditions.checkNotNull(var1, "matcher");
      Preconditions.checkNotNull(var2, "label");
      this.matcher = var1;
      this.name = var2;
      this.description = var3 == null ? "" : var3;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public StringMatcher getMatcher() {
      return this.matcher;
   }
}
