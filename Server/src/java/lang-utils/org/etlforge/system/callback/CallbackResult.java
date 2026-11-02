  
                             
   
package org.etlforge.system.callback;

public interface CallbackResult {
    public void succeeded(Object var1);

    public void failed(Object var1);

    public void cancelled(Object var1);
}

