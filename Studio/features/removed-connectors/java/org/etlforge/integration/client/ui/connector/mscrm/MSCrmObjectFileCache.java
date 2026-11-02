package org.etlforge.integration.client.ui.connector.mscrm;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.connector.ConnectorObjectFileCache;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.io.Folder;

public final class MSCrmObjectFileCache implements MSCrmObjectCache {
   private final ConnectorObjectCache descriptionsCache;
   private final DetailedMetaDataCache detailedMetaDataCache;

   public MSCrmObjectFileCache(Folder var1, long var2, TimeUnit var4) {
      this.detailedMetaDataCache = new DetailedMetaDataCache(var1);
      this.descriptionsCache = new MSCrmObjectFileCache.DescriptionsCache(new ConnectorObjectFileCache(var1, var2, var4));
   }

   @Override
   public void evictOldEntries() {
      this.descriptionsCache.evictOldEntries();
   }

   @Override
   public ConnectorObjectCache getBasicDetailsCache() {
      return this.descriptionsCache;
   }

   @Override
   public Optional<MSCrmObject> getDetailedMetaData(MSCrmEndpoint var1, String var2) {
      return this.detailedMetaDataCache.getMetaData(var1, var2);
   }

   @Override
   public void storeDetailedMetaData(MSCrmEndpoint var1, MSCrmObject var2) {
      this.detailedMetaDataCache.storeMetaData(var1, var2);
   }

   private final class DescriptionsCache implements ConnectorObjectCache {
      private final ConnectorObjectFileCache impl;

      public DescriptionsCache(ConnectorObjectFileCache var2) {
         this.impl = var2;
      }

      @Override
      public void storeObjects(ConnectorObjectCache.Key var1, Iterable<? extends ConnectorObject> var2) {
         this.impl.storeObjects(var1, var2);
         this.removeDetailedMetaData(var1);
      }

      private void removeDetailedMetaData(ConnectorObjectCache.Key var1) {
         MSCrmObjectFileCache.this.detailedMetaDataCache.evict(Collections.singleton(var1.toString()));
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
         ImmutableList var1 = this.impl.evictOldEntries();
         MSCrmObjectFileCache.this.detailedMetaDataCache.evict(var1);
         return var1;
      }
   }
}
