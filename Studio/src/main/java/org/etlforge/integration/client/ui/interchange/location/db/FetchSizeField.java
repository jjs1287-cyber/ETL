package org.etlforge.integration.client.ui.interchange.location.db;

import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.InputPanel;

final class FetchSizeField extends IntValueDefiner {
   public FetchSizeField(InputPanel var1) {
      super(
         var1,
         LocationResources.getString("Database.FetchSize.Name"),
         LocationResources.getString("Database.FetchSize.Label"),
         Integer.MIN_VALUE,
         Integer.MAX_VALUE,
         0
      );
   }

   @Override
   protected void applyValue(DatabaseLocation var1, int var2) {
      var1.setFetchSize(var2);
   }

   public void reset(DatabaseLocation var1) {
      this.setValue(var1.getFetchSize());
   }

   public boolean isDefaultValue() {
      return this.getValue() == 0;
   }
}
