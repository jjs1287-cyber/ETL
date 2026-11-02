package org.etlforge.integration.client.entity.settings;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.persist.Persistor;

public interface EntitySettings {
   Persistor getClassSettings(EntityType var1);

   Persistor getEntitySettings(IntegrationEntity var1);

   void deleteSettings(IntegrationEntity var1);

   void persist();
}
