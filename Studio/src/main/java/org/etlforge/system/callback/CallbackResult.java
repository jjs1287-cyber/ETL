package org.etlforge.system.callback;

public interface CallbackResult {
   void succeeded(Object var1);

   void failed(Object var1);

   void cancelled(Object var1);
}
