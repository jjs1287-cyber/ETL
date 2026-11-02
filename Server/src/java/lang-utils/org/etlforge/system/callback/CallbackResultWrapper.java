  
                             
   
package org.etlforge.system.callback;

import org.etlforge.system.callback.CallbackResult;

public final class CallbackResultWrapper
implements CallbackResult {
    private final CallbackResult wrappee;

    public CallbackResultWrapper(CallbackResult callbackResult) {
        this.wrappee = callbackResult;
    }

    @Override
    public void cancelled(Object object) {
        if (this.wrappee != null) {
            this.wrappee.cancelled(object);
        }
    }

    @Override
    public void failed(Object object) {
        if (this.wrappee != null) {
            this.wrappee.failed(object);
        }
    }

    @Override
    public void succeeded(Object object) {
        if (this.wrappee != null) {
            this.wrappee.succeeded(object);
        }
    }
}

