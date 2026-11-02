  
                             
   
                                        
                                   
   
package org.etlforge.system;

import com.google.common.base.Optional;
import org.etlforge.system.Disposable;

public final class Disposables {
    public static final Disposable NULL = NullObject.INSTANCE;

    public static void dispose(Optional<? extends Disposable> optional) {
        if (optional.isPresent()) {
            ((Disposable)optional.get()).dispose();
        }
    }

    public static void disposeAll(Iterable<? extends Disposable> iterable) {
        for (Disposable disposable : iterable) {
            disposable.dispose();
        }
    }

    private Disposables() {
    }

    private static enum NullObject implements Disposable
    {
        INSTANCE;


        @Override
        public void dispose() {
        }
    }
}

