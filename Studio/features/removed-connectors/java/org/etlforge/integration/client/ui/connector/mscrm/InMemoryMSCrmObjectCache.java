package org.etlforge.integration.client.ui.connector.mscrm;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.connector.InMemoryConnectorObjectCache;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;

public final class InMemoryMSCrmObjectCache implements MSCrmObjectCache {
   private final InMemoryMSCrmObjectCache.DetailedMetaDataCache details = new InMemoryMSCrmObjectCache.DetailedMetaDataCache();
   private final InMemoryMSCrmObjectCache.DescriptionsCache descriptions = new InMemoryMSCrmObjectCache.DescriptionsCache();

   @Override
   public void evictOldEntries() {
      this.descriptions.evictOldEntries();
   }

   @Override
   public ConnectorObjectCache getBasicDetailsCache() {
      return this.descriptions;
   }

   @Override
   public Optional<MSCrmObject> getDetailedMetaData(MSCrmEndpoint var1, String var2) {
      return this.details.lookup(var1, var2);
   }

   @Override
   public void storeDetailedMetaData(MSCrmEndpoint var1, MSCrmObject var2) {
      this.details.store(var1, var2);
   }

   private class DescriptionsCache implements ConnectorObjectCache {
      private final InMemoryConnectorObjectCache impl = new InMemoryConnectorObjectCache();

      private DescriptionsCache() {
      }

      @Override
      public List<ConnectorObject> getObjects(ConnectorObjectCache.Key var1) {
         return this.impl.getObjects(var1);
      }

      @Override
      public void storeObjects(ConnectorObjectCache.Key var1, Iterable<? extends ConnectorObject> var2) {
         this.impl.storeObjects(var1, var2);
      }

      @Override
      public void remove(ConnectorObjectCache.Key var1) {
         this.impl.remove(var1);
         InMemoryMSCrmObjectCache.this.details.evict(Collections.singleton(var1.toString()));
      }

      @Override
      public ImmutableList<String> evictOldEntries() {
         ImmutableList var1 = this.impl.evictOldEntries();
         InMemoryMSCrmObjectCache.this.details.evict(var1);
         return var1;
      }
   }

   private static class DetailedMetaDataCache {
      private final Table<String, String, MSCrmObject> data = HashBasedTable.create();
      private final Object lock = new Object();

      private DetailedMetaDataCache() {
      }

      public Optional<MSCrmObject> lookup(MSCrmEndpoint var1, String var2) {
         String var3 = toFingerprint(var1);
         synchronized (this.lock) {
            return Optional.fromNullable(this.data.get(var3, var2));
         }
      }

      private static String toFingerprint(MSCrmEndpoint var0) {
         return new ConnectorObjectCache.Key(var0).toString();
      }

      public void store(MSCrmEndpoint var1, MSCrmObject var2) {
         String var3 = toFingerprint(var1);
         synchronized (this.lock) {
            this.data.put(var3, var2.getName(), var2);
         }
      }

      public void evict(Iterable<String> var1) {
         synchronized (this.lock) {
            Map var3 = this.data.rowMap();

            for (String var5 : var1) {
               var3.remove(var5);
            }
         }
      }
   }
}
