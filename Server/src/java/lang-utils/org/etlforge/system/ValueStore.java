  
                             
   
                                        
                                   
   
package org.etlforge.system;

import com.google.common.base.Supplier;

public interface ValueStore<V>
extends Supplier<V> {
    public V get();

    public void set(V var1);
}

