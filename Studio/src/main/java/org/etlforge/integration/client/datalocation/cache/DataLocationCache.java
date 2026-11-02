package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.application.cache.DataCache;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.project.IntegrationProject;

public interface DataLocationCache<T extends DataLocation> extends DataCache {
   void update(T var1);

   void update(IntegrationProject var1);
}
