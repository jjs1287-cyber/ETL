package org.etlforge.application.ui.view;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;

public final class ViewActivationResource extends AbstractWindowElementResource {
   private final View view;

   public ViewActivationResource(View var1, WindowElementStateSpan var2) {
      super(var2);
      Preconditions.checkNotNull(var1, "view");
      this.view = var1;
   }

   @Override
   public void create(WindowElement var1) {
      this.view.setActive(true);
   }

   @Override
   public void dispose(WindowElement var1) {
      this.view.setActive(false);
   }
}
