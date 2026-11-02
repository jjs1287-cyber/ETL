package org.etlforge.system.callback;

public final class CallbackResultWrapper implements CallbackResult {
   private final CallbackResult wrappee;

   public CallbackResultWrapper(CallbackResult var1) {
      this.wrappee = var1;
   }

   @Override
   public void cancelled(Object var1) {
      if (this.wrappee != null) {
         this.wrappee.cancelled(var1);
      }
   }

   @Override
   public void failed(Object var1) {
      if (this.wrappee != null) {
         this.wrappee.failed(var1);
      }
   }

   @Override
   public void succeeded(Object var1) {
      if (this.wrappee != null) {
         this.wrappee.succeeded(var1);
      }
   }
}
