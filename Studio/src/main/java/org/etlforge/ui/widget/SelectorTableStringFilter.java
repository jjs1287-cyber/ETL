package org.etlforge.ui.widget;

import com.google.common.base.Predicate;

public abstract class SelectorTableStringFilter<E> implements Predicate<E> {
   private final Predicate<String> condition;

   public SelectorTableStringFilter(Predicate<String> var1) {
      this.condition = var1;
   }

   @Override
   public final boolean apply(E var1) {
      String var2 = this.getString((E)var1);
      return this.condition.apply(var2);
   }

   protected abstract String getString(E var1);
}
