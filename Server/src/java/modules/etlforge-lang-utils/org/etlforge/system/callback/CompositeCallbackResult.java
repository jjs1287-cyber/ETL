package org.etlforge.system.callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeCallbackResult implements CallbackResult {
   private final List<CallbackResult> callbacks;

   public CompositeCallbackResult(CallbackResult... var1) {
      this(Arrays.asList(var1));
   }

   public CompositeCallbackResult(List<CallbackResult> var1) {
      this.callbacks = new ArrayList<>(var1.size());

      for (CallbackResult var3 : var1) {
         if (var3 != null) {
            this.callbacks.add(var3);
         }
      }
   }

   @Override
   public void cancelled(Object var1) {
      for (CallbackResult var3 : this.callbacks) {
         var3.cancelled(var1);
      }
   }

   @Override
   public void failed(Object var1) {
      for (CallbackResult var3 : this.callbacks) {
         var3.failed(var1);
      }
   }

   @Override
   public void succeeded(Object var1) {
      for (CallbackResult var3 : this.callbacks) {
         var3.succeeded(var1);
      }
   }
}
