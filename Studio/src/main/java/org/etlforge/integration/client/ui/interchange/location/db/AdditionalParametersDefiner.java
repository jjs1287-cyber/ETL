package org.etlforge.integration.client.ui.interchange.location.db;

import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.InputPanel;

final class AdditionalParametersDefiner extends StringValueDefiner {
   public AdditionalParametersDefiner(InputPanel var1) {
      super(var1, LocationResources.getString("Database.AdditionalParams.Name"), LocationResources.getString("Database.AdditionalParams.Label"), true, 30, true);
   }

   @Override
   protected void applyValue(DatabaseLocation var1, String var2) {
      var1.setAdditionalConnectionParameters(var2);
   }

   public void reset(DatabaseLocation var1) {
      this.setValue(var1.getAdditionalConnectionParameters());
   }
}
