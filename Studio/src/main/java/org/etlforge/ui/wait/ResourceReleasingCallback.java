package org.etlforge.ui.wait;

import org.etlforge.system.callback.CallbackResult;

public abstract class ResourceReleasingCallback implements CallbackResult {
   private volatile boolean releaseResourceFirst;

   public void setReleaseResourceFirst(boolean var1) {
      this.releaseResourceFirst = var1;
   }

   @Override
   public final void succeeded(final Object var1) {
      this.process(new Runnable() {
         @Override
         public void run() {
            ResourceReleasingCallback.this.onSuccess(var1);
         }
      });
   }

   protected void onSuccess(Object var1) {
   }

   @Override
   public final void cancelled(final Object var1) {
      this.process(new Runnable() {
         @Override
         public void run() {
            ResourceReleasingCallback.this.onCancel(var1);
         }
      });
   }

   protected void onCancel(Object var1) {
   }

   @Override
   public final void failed(final Object var1) {
      this.process(new Runnable() {
         @Override
         public void run() {
            ResourceReleasingCallback.this.onFailure(var1);
         }
      });
   }

   protected void onFailure(Object var1) {
   }

   private void process(Runnable var1) {
      if (this.releaseResourceFirst) {
         this.releaseResource();
      }

      try {
         var1.run();
      } finally {
         if (!this.releaseResourceFirst) {
            this.releaseResource();
         }
      }
   }

   protected abstract void releaseResource();
}
