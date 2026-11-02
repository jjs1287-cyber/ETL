package org.etlforge.integration.client.database;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.collections.Categorizer;
import org.etlforge.integration.client.server.cache.AbstractSingleFileServerCache;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;
import org.xml.sax.SAXException;

public final class DatabaseDriverCache extends AbstractSingleFileServerCache {
   public static final String CACHE_ID = "org.etlforge.integration.data.location.driver.DriverDescriptorServerCache";
   private ImmutableCollection<DriverDescriptor> driverDescriptors = ImmutableSet.of();
   private final DatabaseDriverCacheListeners cacheListeners = new DatabaseDriverCacheListeners();
   private boolean hasBeenLoaded;
   private final Object lock = new Object();

   public DatabaseDriverCache(ServerGuid var1, File var2) {
      super("org.etlforge.integration.data.location.driver.DriverDescriptorServerCache", var1, var2);
   }

   public ImmutableCollection<DriverDescriptor> getDrivers() throws DataCacheException {
      synchronized (this.lock) {
         if (!this.hasBeenLoaded) {
            this.load();
         }

         return this.driverDescriptors;
      }
   }

   void load() throws DataCacheException {
      synchronized (this.lock) {
         File var2 = this.createFile();
         if (var2.exists()) {
            try {
               Set var3 = this.readFromFile(var2);
               this.setDrivers(var3);
               this.hasBeenLoaded = true;
            } catch (IOException var5) {
               throw new DataCacheException(DatabaseMessages.getString("DatabaseDriverCache.LoadingError"), var5);
            }
         }
      }
   }

   public void update(Collection<? extends DriverDescriptor> var1) throws DataCacheException {
      synchronized (this.lock) {
         this.setDrivers(var1);
         this.store();
         this.hasBeenLoaded = true;
      }

      this.fireCacheUpdated(var1);
   }

   private void store() throws DataCacheException {
      try {
         File var1 = this.createFile();
         XmlPersistor var2 = XmlPersistor.newRoot("Drivers");

         for (DriverDescriptor var4 : this.driverDescriptors) {
            Persistor var5 = var2.createChild("Driver");
            DriverDescriptorPersistor.persist(var4, var5);
         }

         var2.store(var1);
      } catch (Exception var6) {
         throw new DataCacheException(DatabaseMessages.getString("DatabaseDriverCache.StoringError"), var6);
      }
   }

   @Override
   protected String getFileName() {
      return "org.etlforge.integration.data.location.driver.DriverDescriptorServerCache";
   }

   public void addCacheListener(DatabaseDriverCacheListener var1) {
      this.cacheListeners.add(var1);
   }

   public void removeCacheListener(DatabaseDriverCacheListener var1) {
      this.cacheListeners.remove(var1);
   }

   private void fireCacheUpdated(Collection<? extends DriverDescriptor> var1) {
      this.cacheListeners.call().cacheWasUpdated(var1);
   }

   private void setDrivers(Collection<? extends DriverDescriptor> var1) {
      if (this.driverDescriptors.isEmpty()) {
         this.driverDescriptors = var1 == null ? ImmutableSet.of() : ImmutableSet.copyOf(var1);
      } else {
         this.replaceDrivers(var1);
      }
   }

   private void replaceDrivers(Collection<? extends DriverDescriptor> var1) {
      Categorizer var2 = Categorizer.forFunction(new DatabaseDriverCache.DriverDivider());
      ImmutableListMultimap var3 = var2.categorize(this.driverDescriptors);
      ImmutableListMultimap var4 = var2.categorize(var1);
      Builder var5 = ImmutableSet.builder();

      for (Object __etlItem117 : var3.keySet()) {
         DriverType var7 = (DriverType) __etlItem117;
         if (!var4.containsKey(var7)) {
            var5.addAll(var3.get(var7));
         }
      }

      var5.addAll(var1);
      this.driverDescriptors = var5.build();
   }

   private Set<DriverDescriptor> readFromFile(File var1) throws IOException {
      HashSet var2 = Sets.newHashSet();

      try {
         XmlPersistor var3 = XmlPersistor.load(var1);

         for (Persistor var5 : var3.getChildren("Driver")) {
            DriverDescriptor var6 = DriverDescriptorPersistor.restore(var5);
            var2.add(var6);
         }
      } catch (SAXException var7) {
         this.oldFormatFile(var1, var7);
      }

      return var2;
   }

   private void oldFormatFile(File var1, Exception var2) {
      var2.printStackTrace();
      if (!var1.delete()) {
         System.err.println("Tried unsuccessfully to delete the file " + var1.getAbsolutePath());
      }
   }

   private static final class DriverDivider implements Function<DriverDescriptor, DriverType> {
      private DriverDivider() {
      }

      public DriverType apply(DriverDescriptor var1) {
         return var1.getType();
      }
   }
}
