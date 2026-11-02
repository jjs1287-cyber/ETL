package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class HttpResponseTargetDefiner implements HttpLocationPanelPart {
   private final InputPanel parent;
   private final HttpLocation loc;
   private ResponseTargetSelector selector;

   HttpResponseTargetDefiner(InputPanel var1, HttpLocation var2) {
      this.parent = var1;
      this.loc = var2;
   }

   public void applyTo(HttpLocation var1) {
      if (this.selector != null) {
         Target var2 = this.selector.getSelectedTarget();
         TargetId var3 = var2 == null ? null : var2.getID();
         var1.setTargetForResponse(var3);
      }
   }

   @Override
   public void dispose() {
      if (this.selector != null) {
         this.selector.dispose();
      }
   }

   @Override
   public void layout(TwoColumnLayout var1) {
      if (this.selector != null) {
         this.selector.layout(Strings.get("Http.ResponseTarget.Label"), var1);
      }
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      if (this.selector != null) {
         TwoColumnPanel var2 = new TwoColumnPanel();
         var2.withLeftFill(TwoColumnPanel.HORIZONTAL);
         var2.withRightAnchor(TwoColumnPanel.SOUTHWEST);
         this.layout(var2);
         var1.add(null, InvisiblePanel.of(var2), true);
      }
   }

   public void setSelector(ResponseTargetSelector var1) {
      this.selector = var1;
      this.reset(this.loc);
      var1.register(this.parent);
   }

   public void reset(HttpLocation var1) {
      if (this.selector != null) {
         this.selector.reset(var1);
      }
   }
}
