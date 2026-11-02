package org.etlforge.integration.client.wsdl.cache;

import org.etlforge.application.cache.DataCacheFactory;
import org.etlforge.integration.client.server.ServerDirectory;

public final class WsdlFileServerCacheFactory implements DataCacheFactory<WsdlFileServerCache> {
   private final WsdlFileServerCache cache;

   public WsdlFileServerCacheFactory(ServerDirectory var1) {
      this.cache = new WsdlFileServerCacheImpl(var1);
   }

   public WsdlFileServerCache get() {
      return this.cache;
   }

   @Override
   public Class<WsdlFileServerCache> getCacheType() {
      return WsdlFileServerCache.class;
   }
}
