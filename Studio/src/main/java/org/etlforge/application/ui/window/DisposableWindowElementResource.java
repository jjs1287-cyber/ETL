package org.etlforge.application.ui.window;

import java.util.Collection;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;

public final class DisposableWindowElementResource extends AbstractWindowElementResource {
   private final DisposableCollection disposables;

   public DisposableWindowElementResource(Disposable... var1) {
      this(WindowElementStateSpan.WHEN_OPEN, var1);
   }

   public DisposableWindowElementResource(Collection<? extends Disposable> var1) {
      this(var1.toArray(new Disposable[var1.size()]));
   }

   public DisposableWindowElementResource(WindowElementStateSpan var1, Disposable... var2) {
      super(var1);
      this.disposables = new DisposableCollection(var2);
   }

   public DisposableWindowElementResource(WindowElementStateSpan var1, Collection<? extends Disposable> var2) {
      this(var1, var2.toArray(new Disposable[var2.size()]));
   }

   @Override
   public void dispose(WindowElement var1) {
      this.disposables.dispose();
   }
}
