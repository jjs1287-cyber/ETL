package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class SalesforceBulkLoadId extends IntegrationEntityId {
   public SalesforceBulkLoadId() {
   }

   public SalesforceBulkLoadId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.SalesforceBulkLoad;
   }
}
