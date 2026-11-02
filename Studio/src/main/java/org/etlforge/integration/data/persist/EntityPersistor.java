package org.etlforge.integration.data.persist;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityPersistor {
   EntityPersistor NONE = new EntityPersistor() {
      @Override
      public void save(IntegrationEntity var1) throws InterchangeSavingException {
      }
   };

   void save(IntegrationEntity var1) throws InterchangeSavingException;
}
