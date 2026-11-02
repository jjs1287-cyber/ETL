package org.etlforge.integration.data.entity.factory;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.metadata.EntityTypeConfig;

public final class DefaultIntegrationEntityFactory implements IntegrationEntityFactory {
   private final EntityType type;

   public DefaultIntegrationEntityFactory(EntityType var1) {
      this.type = Preconditions.checkNotNull(var1, "type");
   }

   @Override
   public IntegrationEntity createEntity(Folder var1) throws IntegrationEntityFactoryException {
      try {
         IntegrationEntity var2 = EntityTypeConfig.createNewEntity(this.type, "");
         this.customize(var2, var1);
         return var2;
      } catch (ClassNotFoundException var3) {
         return this.instantiationError(this.type, var3);
      }
   }

   private void customize(IntegrationEntity var1, IntegrationEntity var2) {
      var1.setTransient(true);
      var1.setName(EntityFactoryUtils.getNewEntityName(var1, var2));
   }

   private IntegrationEntity instantiationError(EntityType var1, Exception var2) throws IntegrationEntityFactoryException {
      String var3 = "The application is not configured to create new instances of type " + var1.getDisplayName() + ". Reported error:\n" + var2.getMessage();
      throw new IntegrationEntityFactoryException(var3, var2);
   }
}
