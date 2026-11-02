package org.etlforge.system;

public interface KeyValue<K, V> {
   V get(K var1, V var2);

   void set(K var1, V var2);
}
