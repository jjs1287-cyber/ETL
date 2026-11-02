package org.etlforge.integration.client.ui.connector.mscrm;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;

public interface MSCrmObjectCache {
   void evictOldEntries();

   ConnectorObjectCache getBasicDetailsCache();

   Optional<MSCrmObject> getDetailedMetaData(MSCrmEndpoint var1, String var2);

   void storeDetailedMetaData(MSCrmEndpoint var1, MSCrmObject var2);
}
