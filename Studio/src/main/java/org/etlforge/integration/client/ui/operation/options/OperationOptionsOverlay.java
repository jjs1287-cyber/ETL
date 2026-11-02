package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.input.InputPanelOverlay;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class OperationOptionsOverlay {
   private final OperationOptionsPanel panel;
   private final Receiver<OperationOptions> receiver;
   private OverlayContainer container;

   public OperationOptionsOverlay(OperationOptionsPanel var1, Receiver<OperationOptions> var2) {
      this.panel = var1;
      var1.setBorder(new BorderBuilder().etched().empty(10));
      this.receiver = var2;
   }

   public void show(OverlayContainer var1) {
      if (this.container == null) {
         this.container = var1;
         OverlayDecorator var2 = this.createOverlay();
         var2.show(var1, new OverlayCenterPositioner());
      }
   }

   private OverlayDecorator createOverlay() {
      InputPanelOverlay var1 = new InputPanelOverlay(this.panel, "Operation Options", true) {
         @Override
         protected void onHidden() {
            OperationOptionsOverlay.this.panel.dispose();
         }

         @Override
         protected void onOk() {
            OperationOptionsOverlay.this.receiver.handle(OperationOptionsOverlay.this.panel.getDefinedOptions());
         }

         @Override
         protected void onCancel() {
            OperationOptionsOverlay.this.receiver.handle(null);
         }
      };
      var1.setFadeIn(AnimationPreference.get());
      var1.setBlocking(true);
      var1.useOkAndCancel();
      return var1;
   }
}
