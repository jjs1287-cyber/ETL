package org.etlforge.application.ui.search.string;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.string.StringMatcher;

public final class DefaultStringSearchModel implements StringSearchModel {
   private String searchString = "";
   private StringSearchRule rule;
   private final List<StringSearchModelListener> listeners = new CopyOnWriteArrayList<>();
   private boolean emptyStringMatchesEverything;

   @Override
   public boolean test(String var1) {
      if (this.rule == null) {
         return false;
      }

      if (this.searchString.length() == 0 && this.emptyStringMatchesEverything) {
         return true;
      }

      StringMatcher var2 = this.rule.getMatcher();
      return var2.matches(this.searchString, var1);
   }

   @Override
   public StringSearchRule getSearchRule() {
      return this.rule;
   }

   public void setSearchRule(StringSearchRule var1) {
      if (!equals(var1, this.rule)) {
         this.rule = var1;

         for (StringSearchModelListener var3 : this.listeners) {
            var3.searchRuleChanged(this, var1);
         }
      }
   }

   @Override
   public String getSearchString() {
      return this.searchString;
   }

   public boolean isEmpty() {
      return this.searchString.isEmpty();
   }

   public void setSearchString(String var1) {
      String var2 = var1 != null ? var1 : "";
      if (!this.searchString.equals(var2)) {
         this.searchString = var2;

         for (StringSearchModelListener var4 : this.listeners) {
            var4.searchStringChanged(this, var1);
         }
      }
   }

   public void setEmptyStringMatchesEverything(boolean var1) {
      this.emptyStringMatchesEverything = var1;
   }

   @Override
   public void addModelListener(StringSearchModelListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeModelListener(StringSearchModelListener var1) {
      this.listeners.remove(var1);
   }

   private static boolean equals(Object var0, Object var1) {
      return var0 != null ? var0.equals(var1) : var1 == null;
   }
}
