package org.etlforge.system;

public final class EmptyKeyValue<K, V> implements KeyValue<K, V> {
   public static <O, P> KeyValue<O, P> create() {
      return new EmptyKeyValue();
   }

   @Override
   public V get(K var1, V var2) {
      return (V)var2;
   }

   @Override
   public void set(K var1, V var2) {
   }
}
