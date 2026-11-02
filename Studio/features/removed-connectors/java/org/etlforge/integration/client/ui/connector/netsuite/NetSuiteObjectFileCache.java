package org.etlforge.integration.client.ui.connector.netsuite;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.connector.ConnectorObjectFileCache;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.io.Folder;

public final class NetSuiteObjectFileCache implements NetSuiteObjectCache {
   private final ConnectorObjectCache descriptionsCache;
   private final NetSuiteDefinitionDesignFileCache defCache;
   private final NetSuiteCustomFieldCache cfCache;

   public NetSuiteObjectFileCache(Folder var1, long var2, TimeUnit var4) {
      this.descriptionsCache = new NetSuiteObjectFileCache.DescriptionsCache(new ConnectorObjectFileCache(var1, var2, var4));
      this.defCache = new NetSuiteDefinitionDesignFileCache(var1);
      this.cfCache = new NetSuiteCustomFieldFileCache(var1);
   }

   @Override
   public void evictOldEntries() {
      this.descriptionsCache.evictOldEntries();
   }

   @Override
   public ConnectorObjectCache getObjectListCache() {
      return this.descriptionsCache;
   }

   @Override
   public NetSuiteDefinitionDesignFileCache getNetSuiteDefinitionDesignFileCache() {
      return this.defCache;
   }

   @Override
   public NetSuiteCustomFieldCache getNetSuiteCustomFieldCache() {
      return this.cfCache;
   }

   private final class DescriptionsCache implements ConnectorObjectCache {
      private final ConnectorObjectFileCache impl;

      public DescriptionsCache(ConnectorObjectFileCache var2) {
         this.impl = var2;
      }

      @Override
      public void storeObjects(ConnectorObjectCache.Key var1, Iterable<? extends ConnectorObject> var2) {
         this.impl.storeObjects(var1, var2);
         NetSuiteObjectFileCache.this.defCache.evict(var1);
      }

      @Override
      public void remove(ConnectorObjectCache.Key var1) {
         this.impl.remove(var1);
      }

      @Override
      public List<ConnectorObject> getObjects(ConnectorObjectCache.Key var1) {
         return this.impl.getObjects(var1);
      }

      @Override
      public ImmutableList<String> evictOldEntries() {
         return this.impl.evictOldEntries();
      }
   }
}
