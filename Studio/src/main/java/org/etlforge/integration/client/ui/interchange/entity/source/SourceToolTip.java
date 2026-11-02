package org.etlforge.integration.client.ui.interchange.entity.source;

import org.etlforge.integration.client.ui.interchange.locatable.DataLocationToolTipEntries;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class SourceToolTip extends IntegrationEntityToolTip {
   public SourceToolTip(Source var1) {
      super(var1);
      this.addType(var1);
   }

   private void addType(Source var1) {
      DataLocation var2 = var1.getLocation();
      if (var2 != null) {
         DataLocationToolTipEntries.addTo(this, var2, SourceTarget.Source);
      }
   }
}
