package org.etlforge.integration.data.functors;

import com.google.common.base.Function;
import java.io.Serializable;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EntityFunctions {
   public static final Function<IntegrationEntity, EntityType> TO_TYPE = new EntityFunctions.EntityToType();
   public static Function<IntegrationEntity, EntityDescriptor> TO_DESCRIPTOR = new EntityFunctions.ToDescriptorImpl();

   private EntityFunctions() {
   }

   private static class EntityToType implements Function<IntegrationEntity, EntityType>, Serializable {
      private EntityToType() {
      }

      public EntityType apply(IntegrationEntity var1) {
         return var1.getEntityType();
      }
   }

   private static class ToDescriptorImpl implements Function<IntegrationEntity, EntityDescriptor> {
      private ToDescriptorImpl() {
      }

      public EntityDescriptor apply(IntegrationEntity var1) {
         return new EntityDescriptor(var1);
      }
   }
}
