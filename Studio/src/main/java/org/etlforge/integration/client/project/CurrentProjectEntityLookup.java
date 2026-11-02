package org.etlforge.integration.client.project;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;

public final class CurrentProjectEntityLookup implements IntegrationEntityLookup {
   private final ProjectManager pm;

   public CurrentProjectEntityLookup(ProjectManager var1) {
      this.pm = var1;
   }

   @Override
   public IntegrationEntity getEntity(IntegrationEntityId var1) {
      return this.getDelegate().getEntity(var1);
   }

   private IntegrationEntityLookup getDelegate() {
      ManagedProject var1 = this.pm.getCurrentProject();
      return var1 == null ? IntegrationEntityLookup.NULL : var1.getProject().getItemLookup();
   }

   @Override
   public <T extends IntegrationEntity> T getEntity(IntegrationEntityId var1, Class<T> var2) {
      return this.getDelegate().getEntity(var1, var2);
   }
}
