package org.etlforge.integration.client.ui.connector.netsuite;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;

public class NetSuiteDefinitionDesignMemoryCache implements NetSuiteDefinitionDesignCache {
   private final Map<String, NetSuiteDefinitionDesign> cache = Maps.newHashMap();
   private final int limit;

   public NetSuiteDefinitionDesignMemoryCache(int var1) {
      this.limit = var1;
   }

   @Override
   public Optional<NetSuiteDefinitionDesign> getNetSuiteDefinitionDesign(NetSuiteEndpoint var1) {
      String var2 = NetSuiteDefinitionDesignFileCache.getFileName(var1);
      if (this.cache.containsKey(var2)) {
         NetSuiteDefinitionDesign var3 = this.cache.get(var2);
         return Optional.of(var3);
      } else {
         return Optional.absent();
      }
   }

   @Override
   public void storeNetSuiteDefinitionDesign(NetSuiteEndpoint var1, NetSuiteDefinitionDesign var2) {
      if (var2 != null) {
         String var3 = NetSuiteDefinitionDesignFileCache.getFileName(var1);
         if (this.cache.size() >= this.limit) {
            this.cache.clear();
         }

         this.cache.put(var3, var2);
      }
   }

   public void evictCache(File var1) {
      String var2 = var1.getName();
      this.cache.remove(var2);
   }
}
