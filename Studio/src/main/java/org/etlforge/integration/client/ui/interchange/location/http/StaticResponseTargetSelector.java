package org.etlforge.integration.client.ui.interchange.location.http;

import javax.swing.JLabel;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class StaticResponseTargetSelector implements ResponseTargetSelector {
   private final IntegrationEntityLookup targetLookup;
   private final JLabel label;
   private Target target;

   public StaticResponseTargetSelector(IntegrationEntityLookup var1) {
      this.targetLookup = var1;
      this.label = new JLabel();
   }

   @Override
   public Target getSelectedTarget() {
      return this.target;
   }

   @Override
   public void layout(String var1, TwoColumnLayout var2) {
      var2.addRow(BoxBuilder.horizontal().addAllWithSpace(5, var1, this.label).container());
   }

   @Override
   public void reset(HttpLocation var1) {
      this.target = this.targetLookup.getEntity(var1.getTargetIdForResponse(), Target.class);
      if (this.target != null) {
         RendererUtils.decorateRenderer(this.label, this.target, false);
      } else {
         this.label.setIcon(null);
         this.label.setText("[None]");
      }
   }

   @Override
   public void register(InputPanel var1) {
   }

   @Override
   public void dispose() {
   }
}
