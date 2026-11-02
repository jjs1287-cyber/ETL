package org.etlforge.integration.client.ui.interchange.location.db;

import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.InputPanel;

class TimeOutDefiner extends IntValueDefiner {
   TimeOutDefiner(InputPanel var1) {
      super(var1, LocationResources.getString("Database.Timeout.Name"), LocationResources.getString("Database.Timeout.Label"), 0, 10000000, 300);
   }

   public void reset(DatabaseLocation var1) {
      this.setValue(var1.getTimeout());
   }

   @Override
   protected void applyValue(DatabaseLocation var1, int var2) {
      var1.setTimeout(var2);
   }
}
