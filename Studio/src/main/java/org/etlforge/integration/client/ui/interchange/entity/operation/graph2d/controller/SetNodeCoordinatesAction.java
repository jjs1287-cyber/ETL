package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanelOverlay;
import org.etlforge.ui.util.PointInputDialog;
import org.etlforge.ui.util.PointInputPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class SetNodeCoordinatesAction extends AbstractAction {
   private static final boolean USE_OVERLAY = true;
   private final OperationGraphController controller;
   private final Object node;

   public SetNodeCoordinatesAction(OperationGraphController var1, Object var2) {
      super("Set Node Coordinates...");
      this.controller = var1;
      this.node = var2;
      this.putValue("ShortDescription", "Enter a new location for the selected node");
      this.setEnabled(this.isApplicable(var2));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      Point var2 = this.getCurrentLocation();
      new SetNodeCoordinatesAction.OverlayPrompt().run(var2);
   }

   private boolean isApplicable(Object var1) {
      return var1 instanceof OperationWrapperNode || var1 instanceof EmailWrapperNode;
   }

   private Point getCurrentLocation() {
      Rectangle var1 = this.controller.getGraph().getLayout().getBounds(this.node);
      return var1 != null ? var1.getLocation() : null;
   }

   private void applyNewPoint(Point var1) {
      if (var1 != null) {
         this.controller.moveNodeTo(this.node, var1);
      }
   }

   private class DialogPrompt {
      public void run(Point var1) {
         Point var2 = this.getNewLocation(var1);
         SetNodeCoordinatesAction.this.applyNewPoint(var2);
      }

      private Point getNewLocation(Point var1) {
         PointInputDialog var2 = new PointInputDialog(UiUtils.getActiveWindow(), "Set New Coordinates");
         var2.setCaption("Enter the new coordinates for the node:");
         var2.setInitialValue(var1);
         return var2.open();
      }
   }

   private class OverlayPrompt {
      private PointInputPanel inputPanel;

      private OverlayPrompt() {
      }

      public void run(Point var1) {
         OverlayDecorator var2 = this.createOverlay(var1);
         var2.show(this.getContainer(), new OverlayCenterPositioner());
         this.inputPanel.setInitialFocus();
      }

      private OverlayContainer getContainer() {
         return SetNodeCoordinatesAction.this.controller.getGraph().getOverlayContainer();
      }

      private OverlayDecorator createOverlay(Point var1) {
         this.inputPanel = new PointInputPanel(var1, null);
         InputPanelOverlay var2 = new InputPanelOverlay(this.inputPanel, "Set New Coordinates") {
            @Override
            protected void onOk() {
               Point var1x = OverlayPrompt.this.inputPanel.isInputValid() ? OverlayPrompt.this.inputPanel.getNewCoordinates() : null;
               SetNodeCoordinatesAction.this.applyNewPoint(var1x);
            }
         };
         var2.setFadeIn(AnimationPreference.get());
         var2.useOkAndCancel();
         this.bindOkAction(var2);
         return var2;
      }

      private void bindOkAction(final OverlayDecorator var1) {
         this.inputPanel.addActionListener(var1.getOkAction());
         this.inputPanel.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1x) {
               var1.getOkAction().setEnabled(OverlayPrompt.this.inputPanel.isInputValid());
            }
         });
      }
   }
}
