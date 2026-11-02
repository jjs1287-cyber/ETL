package org.etlforge.application.ui.search.string;

import com.google.common.base.Function;
import org.etlforge.string.StringMatcher;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.OptionSelector;

public final class SearchRuleOption implements StringSearchRule, Option {
   public static final Function<StringSearchRule, SearchRuleOption> CONVERTER = new Function<StringSearchRule, SearchRuleOption>() {
      public SearchRuleOption apply(StringSearchRule var1) {
         return new SearchRuleOption(var1);
      }
   };
   private final StringSearchRule impl;

   public SearchRuleOption(StringSearchRule var1) {
      this.impl = var1;
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(this.getName());
      var1.setDescription(this.getDescription());
   }

   @Override
   public String getName() {
      return this.impl.getName();
   }

   @Override
   public String getDescription() {
      return this.impl.getDescription();
   }

   @Override
   public StringMatcher getMatcher() {
      return this.impl.getMatcher();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof SearchRuleOption ? this.impl.equals(((SearchRuleOption)var1).impl) : var1 == this.impl;
      }
   }

   @Override
   public int hashCode() {
      return this.impl.hashCode();
   }
}
