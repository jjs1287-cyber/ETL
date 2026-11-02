package org.etlforge.application.ui.view;

import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;

public final class ViewDestroyingResource extends AbstractWindowElementResource {
   private final ViewManager viewManager;
   private final String viewId;

   public ViewDestroyingResource(ViewManager var1, View var2, WindowElementStateSpan var3) {
      super(var3);
      this.viewManager = var1;
      this.viewId = var2.getID();
   }

   @Override
   public void dispose(WindowElement var1) {
      this.viewManager.removeView(this.viewId);
   }
}
