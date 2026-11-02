package org.etlforge.system;

import com.google.common.base.Optional;

public final class Disposables {
   public static final Disposable NULL = Disposables.NullObject.INSTANCE;

   public static void dispose(Optional<? extends Disposable> var0) {
      if (var0.isPresent()) {
         ((Disposable)var0.get()).dispose();
      }
   }

   public static void disposeAll(Iterable<? extends Disposable> var0) {
      for (Disposable var2 : var0) {
         var2.dispose();
      }
   }

   private Disposables() {
   }

   private enum NullObject implements Disposable {
      INSTANCE;

      @Override
      public void dispose() {
      }
   }
}
