package org.etlforge.ui.selection;

import com.google.common.collect.Lists;
import java.util.Collection;

public final class SimpleSelection implements Selection {
   private final Collection<Object> items;

   public SimpleSelection(Object... var1) {
      this.items = Lists.newArrayList(var1);
   }

   public SimpleSelection(Collection<?> var1) {
      this.items = Lists.newArrayList(var1);
   }

   public SimpleSelection(Iterable<?> var1) {
      this.items = Lists.newArrayList(var1);
   }

   public SimpleSelection(Selection var1) {
      this.items = Lists.newArrayList(var1.items());
   }

   @Override
   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   @Override
   public Iterable<? extends Object> items() {
      return Lists.newArrayList(this.items);
   }
}
