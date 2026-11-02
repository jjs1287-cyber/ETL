package org.etlforge.integration.data.location;

import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.IntegrationData;

final class ExportableManualConnectionString implements ExportableProperty {
   private final DatabaseLocation origin;

   public ExportableManualConnectionString(DatabaseLocation var1) {
      this.origin = var1;
   }

   @Override
   public String getName() {
      return LocationMessages.getString("DatabaseLocation.Property.ManualConnectionString");
   }

   @Override
   public boolean isExportedByDefault() {
      return true;
   }

   @Override
   public boolean exportTo(IntegrationData var1) {
      this.exportTo((DatabaseLocation)var1);
      return true;
   }

   private void exportTo(DatabaseLocation var1) {
      boolean var2 = this.origin.isManualMode();
      var1.setManualMode(var2);
      String var3 = this.origin.getManualConnectionString();
      var1.setManualConnectionString(var3);
   }
}
