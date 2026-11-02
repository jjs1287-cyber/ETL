package org.etlforge.util;

public interface ObservableValue<T> {
   T get();

   void addListener(ObservableValue.Listener<? super T> var1);

   void removeListener(ObservableValue.Listener<? super T> var1);

   interface Listener<V> {
      void valueChanged(ObservableValue<? extends V> var1, V var2, V var3);
   }
}
