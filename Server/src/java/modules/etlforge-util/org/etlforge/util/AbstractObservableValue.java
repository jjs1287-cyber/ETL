package org.etlforge.util;

import com.google.common.collect.Lists;
import java.util.List;

public abstract class AbstractObservableValue<V> implements ObservableValue<V> {
   private final List<ObservableValue.Listener<? super V>> listeners = Lists.newCopyOnWriteArrayList();

   @Override
   public final void addListener(ObservableValue.Listener<? super V> var1) {
      this.listeners.add(var1);
   }

   @Override
   public final void removeListener(ObservableValue.Listener<? super V> var1) {
      this.listeners.remove(var1);
   }

   protected final void fireValueChange(V var1, V var2) {
      if (var1 != var2) {
         for (ObservableValue.Listener var4 : this.listeners) {
            var4.valueChanged(this, (V)var1, (V)var2);
         }
      }
   }
}
