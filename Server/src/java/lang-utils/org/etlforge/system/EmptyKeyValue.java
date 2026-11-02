  
                             
   
package org.etlforge.system;

import org.etlforge.system.KeyValue;

public final class EmptyKeyValue<K, V>
implements KeyValue<K, V> {
    public static <O, P> KeyValue<O, P> create() {
        return new EmptyKeyValue();
    }

    @Override
    public V get(K k, V v) {
        return v;
    }

    @Override
    public void set(K k, V v) {
    }
}

