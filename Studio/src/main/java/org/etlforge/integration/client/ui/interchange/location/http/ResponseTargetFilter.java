package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.HttpLocation;

final class ResponseTargetFilter implements EntityFilter {
   private final HttpLocation thisLocation;

   public ResponseTargetFilter(HttpLocation var1) {
      this.thisLocation = var1;
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return var1 instanceof Target ? this.accept((Target)var1) : false;
   }

   private boolean accept(Target var1) {
      DataLocation var2 = var1.getLocation();
      return var2 == null || this.accept(var2);
   }

   private boolean accept(DataLocation var1) {
      return var1 != this.thisLocation && var1.isFileBased();
   }
}
