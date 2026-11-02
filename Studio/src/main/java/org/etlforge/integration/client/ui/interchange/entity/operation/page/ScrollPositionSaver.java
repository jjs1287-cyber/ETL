package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.ui.widget.scroll.ScrollPosition;

final class ScrollPositionSaver extends AbstractWindowElementResource {
   private final OperationPage page;

   public ScrollPositionSaver(OperationPage var1) {
      this.page = var1;
   }

   @Override
   public void dispose(WindowElement var1) {
      ScrollPosition var2 = this.page.getGraph().getScrollPosition();
      this.page.getLayoutStore().saveScrollPosition(var2);
   }
}
