package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public class ExpandedOptionsOverlay {
   private final ExpandedOptionsPanel panel;
   private OverlayContainer container;

   public ExpandedOptionsOverlay(OperationGraphController var1, OperationWrapperNode var2) {
      this.panel = new ExpandedOptionsPanel(var1, var2);
   }

   public void show(OverlayContainer var1) {
      if (this.container == null) {
         this.container = var1;
         OverlayDecorator var2 = new OverlayDecorator(this.panel, "Expanded Operation Options") {
            @Override
            protected void onHidden() {
               ExpandedOptionsOverlay.this.panel.dispose();
            }

            @Override
            protected void onOk() {
               ExpandedOptionsOverlay.this.panel.apply();
            }
         };
         var2.setBlocking(true);
         var2.useOkAndCancel();
         var2.show(var1, new OverlayCenterPositioner());
      }
   }
}
