package org.etlforge.integration.data.location.driver;

import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LocationMessages;

public final class ExportableDriverProperty implements ExportableProperty {
   private final DatabaseLocation origin;

   public ExportableDriverProperty(DatabaseLocation var1) {
      this.origin = var1;
   }

   @Override
   public boolean exportTo(IntegrationData var1) {
      if (var1 instanceof DatabaseLocation) {
         ((DatabaseLocation)var1).setDriver(this.origin.getDriver());
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String getName() {
      return LocationMessages.getString("ExportableDriverProperty.Name");
   }

   @Override
   public boolean isExportedByDefault() {
      return true;
   }
}
