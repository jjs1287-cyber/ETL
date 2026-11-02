package org.etlforge.integration.client.ui.interchange.entity.project;

import java.io.File;
import java.util.Date;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class IntegrationProjectToolTip extends IntegrationEntityToolTip {
   public IntegrationProjectToolTip(IntegrationProject var1) {
      super(var1, false);
      this.addProperties(var1);
   }

   private void addProperties(IntegrationProject var1) {
      this.addLocation(var1);
      this.addModifiedAt(var1);
   }

   private void addLocation(IntegrationProject var1) {
      File var2 = var1.getLocationOnDisk();
      if (var2 != null) {
         this.addEntry("Location on disk:", var2);
      }
   }

   private void addModifiedAt(IntegrationProject var1) {
      Date var2 = var1.getModifiedAt();
      if (var2 != null) {
         this.addEntry("Last modified:", var2);
      }
   }
}
