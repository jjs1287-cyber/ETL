package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.panel.IgnoreLinesPanel;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

class IgnoreLinesDefiner implements HttpLocationPanelPart {
   private final IgnoreLinesPanel panel = new IgnoreLinesPanel();

   public IgnoreLinesDefiner(InputPanel var1) {
      var1.addInputPanel(this.panel);
   }

   @Override
   public void layout(TwoColumnLayout var1) {
      var1.addRow(this.panel.getUi());
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(null, this.panel.getUi(), true);
   }

   public void applyTo(HttpLocation var1) {
      this.panel.apply(var1);
   }

   @Override
   public void dispose() {
      this.panel.dispose();
   }

   public void reset(HttpLocation var1) {
      this.panel.reset(var1);
   }
}
