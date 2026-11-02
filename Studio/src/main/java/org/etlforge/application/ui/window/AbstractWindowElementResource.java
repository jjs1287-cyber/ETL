package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;

public abstract class AbstractWindowElementResource implements WindowElementResource {
   private final WindowElementStateSpan span;

   protected AbstractWindowElementResource() {
      this(WindowElementStateSpan.WHEN_OPEN);
   }

   protected AbstractWindowElementResource(WindowElementStateSpan var1) {
      this.span = Preconditions.checkNotNull(var1);
   }

   @Override
   public final WindowElementStateSpan getLifeSpan() {
      return this.span;
   }

   @Override
   public void create(WindowElement var1) {
   }

   @Override
   public void dispose(WindowElement var1) {
   }
}
