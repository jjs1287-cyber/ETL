  
                             
   
package org.etlforge.system.callback;

import org.etlforge.system.callback.CallbackResult;

public abstract class CallbackResultAdapter
implements CallbackResult {
    @Override
    public void succeeded(Object object) {
    }

    @Override
    public void failed(Object object) {
    }

    @Override
    public void cancelled(Object object) {
    }
}

