package org.etlforge.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableBoolean extends AbstractObservableValue<Boolean> implements Serializable {
   private final AtomicBoolean value;

   public ObservableBoolean(boolean var1) {
      this.value = new AtomicBoolean(var1);
   }

   public Boolean get() {
      return this.value.get();
   }

   public void set(boolean var1) {
      boolean var2 = this.value.getAndSet(var1);
      if (var2 != var1) {
         this.fireValueChange(var2, var1);
      }
   }

   @Override
   public int hashCode() {
      return this.value.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof ObservableBoolean && this.get() == ((ObservableBoolean)var1).get();
   }

   @Override
   public String toString() {
      return this.value.toString();
   }
}
