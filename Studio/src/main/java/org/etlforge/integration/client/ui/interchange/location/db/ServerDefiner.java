package org.etlforge.integration.client.ui.interchange.location.db;

import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.InputPanel;

final class ServerDefiner extends StringValueDefiner {
   public ServerDefiner(InputPanel var1) {
      super(var1, LocationResources.getString("Database.Server.Name"), LocationResources.getString("Database.Server.Label"), false, 20, false);
   }

   public void reset(DatabaseLocation var1) {
      this.setValue(var1.getServer());
   }

   @Override
   protected void applyValue(DatabaseLocation var1, String var2) {
      var1.setServer(var2);
   }
}
