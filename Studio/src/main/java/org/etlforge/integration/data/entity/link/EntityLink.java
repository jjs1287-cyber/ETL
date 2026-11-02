package org.etlforge.integration.data.entity.link;

import com.google.common.base.Function;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.Disposable;

public interface EntityLink extends Disposable {
   Function<EntityLink, IntegrationEntity> TO_DEPENDANT = new Function<EntityLink, IntegrationEntity>() {
      public IntegrationEntity apply(EntityLink var1) {
         return var1.getDependantObject();
      }
   };

   IntegrationEntity getMainObject();

   IntegrationEntity getDependantObject();

   boolean isStrong();

   @Override
   void dispose();

   IntegrationEntity getOtherObject(IntegrationEntity var1);
}
