package org.etlforge.integration.client.entity.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.entity.IntegrationEntity;

public enum TargetFunctions implements Function<IntegrationEntity, IntegrationData> {
   IDENTITY {
      public IntegrationData apply(IntegrationEntity var1) {
         return var1;
      }
   };

   TargetFunctions() {
   }
}
