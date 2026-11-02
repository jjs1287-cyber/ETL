package org.etlforge.integration.client.server.cache;

import org.etlforge.application.cache.DataCache;
import org.etlforge.integration.common.server.ServerGuid;

public interface ServerCacheListener<T extends DataCache> {
   void cacheAdded(ServerGuid var1, T var2);
}
