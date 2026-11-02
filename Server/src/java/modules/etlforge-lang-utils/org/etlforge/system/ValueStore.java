package org.etlforge.system;

import com.google.common.base.Supplier;

public interface ValueStore<V> extends Supplier<V> {
   @Override
   V get();

   void set(V var1);
}
