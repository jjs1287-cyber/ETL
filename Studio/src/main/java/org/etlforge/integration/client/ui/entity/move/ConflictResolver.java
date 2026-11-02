package org.etlforge.integration.client.ui.entity.move;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface ConflictResolver {
   ConflictResolver DO_NOT_MOVE = new ConflictResolver() {
      @Override
      public boolean resolve(IntegrationEntity var1, IntegrationEntity var2, IntegrationEntity var3) {
         return false;
      }
   };

   boolean resolve(IntegrationEntity var1, IntegrationEntity var2, IntegrationEntity var3);
}
