package org.etlforge.integration.client.ui.interchange.location.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class TargetFunctions {
   public static final Function<IntegrationEntity, IntegrationData> TO_LOCATION = new TargetFunctions.ToLocation();

   private TargetFunctions() {
   }

   private static class ToLocation implements Function<IntegrationEntity, IntegrationData> {
      private ToLocation() {
      }

      public IntegrationData apply(IntegrationEntity var1) {
         return ((Locatable)var1).getLocation();
      }
   }
}
