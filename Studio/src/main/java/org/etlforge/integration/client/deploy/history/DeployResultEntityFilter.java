package org.etlforge.integration.client.deploy.history;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class DeployResultEntityFilter implements DeployResultFilter {
   private final IntegrationEntityId entityId;

   public DeployResultEntityFilter(IntegrationEntity var1) {
      this.entityId = var1.getID();
   }

   public DeployResultEntityFilter(IntegrationEntityId var1) {
      Preconditions.checkNotNull(var1, "entityId");
      this.entityId = var1;
   }

   @Override
   public boolean apply(DeployResult var1) {
      DeployedEntityDescriptor var2 = var1.getDeployedDataRoot();
      return this.check(var2);
   }

   private boolean check(DeployedEntityDescriptor var1) {
      if (this.entityId.equals(var1.getId())) {
         return true;
      }

      for (DeployedEntityDescriptor var3 : var1.getChildren()) {
         if (this.check(var3)) {
            return true;
         }
      }

      return false;
   }
}
