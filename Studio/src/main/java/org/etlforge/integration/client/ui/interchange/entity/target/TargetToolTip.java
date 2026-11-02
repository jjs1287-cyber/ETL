package org.etlforge.integration.client.ui.interchange.entity.target;

import org.etlforge.integration.client.ui.interchange.locatable.DataLocationToolTipEntries;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class TargetToolTip extends IntegrationEntityToolTip {
   public TargetToolTip(Target var1) {
      super(var1);
      this.addType(var1);
   }

   private void addType(Target var1) {
      DataLocation var2 = var1.getLocation();
      if (var2 != null) {
         DataLocationToolTipEntries.addTo(this, var2, SourceTarget.Target);
      }
   }
}
