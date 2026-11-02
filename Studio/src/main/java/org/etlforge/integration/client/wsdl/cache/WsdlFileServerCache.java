package org.etlforge.integration.client.wsdl.cache;

import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.cache.ServerCache;

public interface WsdlFileServerCache extends ServerCache<WsdlFileCache> {
   void cacheWsdlFile(ServerInfo var1, String var2, byte[] var3) throws DataCacheException;
}
