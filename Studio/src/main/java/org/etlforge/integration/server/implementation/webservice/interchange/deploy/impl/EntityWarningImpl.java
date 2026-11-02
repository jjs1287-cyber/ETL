package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import org.etlforge.integration.client.deploy.serverapi.EntityWarning;
import org.etlforge.integration.data.entity.IntegrationEntityId;

final class EntityWarningImpl implements EntityWarning {
   private final IntegrationEntityId id;
   private final String warning;

   public EntityWarningImpl(IntegrationEntityId var1, String var2) {
      this.id = var1;
      this.warning = var2 != null ? var2 : "";
   }

   @Override
   public IntegrationEntityId getIntegrationEntityId() {
      return this.id;
   }

   @Override
   public String getWarning() {
      return this.warning;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof EntityWarning ? this.equals((EntityWarning)var1) : false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.id.hashCode();
      return 31 * var1 + this.warning.hashCode();
   }

   private boolean equals(EntityWarning var1) {
      return this.id.equals(var1.getIntegrationEntityId()) && this.warning.equals(var1.getWarning());
   }
}
