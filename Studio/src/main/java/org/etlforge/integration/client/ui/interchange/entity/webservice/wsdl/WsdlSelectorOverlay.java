package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class WsdlSelectorOverlay extends OverlayDecorator {
   private final WsdlSelectorPanel ui;
   private final WsdlSelectorOverlay.UiListener listener;

   public WsdlSelectorOverlay(WsdlSelectorPanel var1) {
      super(new BorderLayoutBuilder(var1).withBorder(Empty.border(5)).container(), "Select a WSDL");
      this.ui = var1;
      this.setBlocking(true);
      this.useOkAndCancel();
      this.setOkAsDefault(true);
      this.updateEnabledState();
      this.listener = new WsdlSelectorOverlay.UiListener();
      var1.addChangeListener(this.listener);
      var1.addActionListener(this.listener);
   }

   private void updateEnabledState() {
      this.getOkAction().setEnabled(this.ui.inputIsValid());
   }

   @Override
   protected void onHidden() {
      this.ui.removeChangeListener(this.listener);
      this.ui.removeActionListener(this.listener);
      this.ui.dispose();
   }

   private class UiListener implements PropertyChangeListener, ActionListener {
      private UiListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         WsdlSelectorOverlay.this.updateEnabledState();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Action var2 = WsdlSelectorOverlay.this.getOkAction();
         if (var2.isEnabled()) {
            var2.actionPerformed(var1);
         }
      }
   }
}
