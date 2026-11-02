  
                             
   
package org.etlforge.system.callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etlforge.system.callback.CallbackResult;

public class CompositeCallbackResult
implements CallbackResult {
    private final List<CallbackResult> callbacks;

    public CompositeCallbackResult(CallbackResult ... callbackResultArray) {
        this(Arrays.asList(callbackResultArray));
    }

    public CompositeCallbackResult(List<CallbackResult> list) {
        this.callbacks = new ArrayList<CallbackResult>(list.size());
        for (CallbackResult callbackResult : list) {
            if (callbackResult == null) continue;
            this.callbacks.add(callbackResult);
        }
    }

    @Override
    public void cancelled(Object object) {
        for (CallbackResult callbackResult : this.callbacks) {
            callbackResult.cancelled(object);
        }
    }

    @Override
    public void failed(Object object) {
        for (CallbackResult callbackResult : this.callbacks) {
            callbackResult.failed(object);
        }
    }

    @Override
    public void succeeded(Object object) {
        for (CallbackResult callbackResult : this.callbacks) {
            callbackResult.succeeded(object);
        }
    }
}

