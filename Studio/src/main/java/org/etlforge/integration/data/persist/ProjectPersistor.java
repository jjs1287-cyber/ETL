package org.etlforge.integration.data.persist;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface ProjectPersistor extends EntityPersistor {
   @Override
   void save(IntegrationEntity var1) throws InterchangeSavingException;

   void saveProjectRelations() throws InterchangeSavingException;
}
