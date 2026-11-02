package org.etlforge.integration.client.database;

import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.cache.ServerCache;

public interface DatabaseDriverServerCache extends ServerCache<DatabaseDriverCache> {
   DatabaseDriverCache createCache(ServerInfo var1);
}
