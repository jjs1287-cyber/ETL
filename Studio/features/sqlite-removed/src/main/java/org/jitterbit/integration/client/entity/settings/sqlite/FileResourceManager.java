package org.jitterbit.integration.client.entity.settings.sqlite;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import org.jitterbit.integration.client.entity.settings.FileResource;
import org.jitterbit.integration.data.entity.IntegrationEntity;
import org.jitterbit.integration.data.entity.IntegrationEntityId;

final class FileResourceManager {
   private final FileResourcesTable table;
   private final ReadWriteLock lock;
   private final Map<IntegrationEntity, FileResourceManager.EntityRoot> roots = Maps.newHashMap();

   public FileResourceManager(FileResourcesTable var1, ReadWriteLock var2) {
      this.table = var1;
      this.lock = var2;
   }

   public synchronized SQLiteFileResource getResource(IntegrationEntity var1, String var2) {
      FileResourceManager.EntityRoot var3 = this.roots.get(var1);
      if (var3 == null) {
         var3 = new FileResourceManager.EntityRoot(var1);
         this.roots.put(var1, var3);
      }

      return var3.get(var2);
   }

   public synchronized void cleanup() {
      for (Entry var2 : this.roots.entrySet()) {
         IntegrationEntity var3 = (IntegrationEntity)var2.getKey();
         FileResourceManager.EntityRoot var4 = (FileResourceManager.EntityRoot)var2.getValue();

         for (FileResource var6 : var4.getAll()) {
            if (var3.isDeleted() || var6.getLifeTime() == FileResource.LifeTime.PROJECT_SESSION) {
               var6.delete();
            }
         }
      }
   }

   public synchronized void exportTo(IntegrationEntity var1, FileResourceManager var2) {
      synchronized (var2) {
         var2.lock.writeLock().lock();

         try {
            var2.deleteResourcesForEntity(var1);
            this.lock.readLock().lock();

            try {
               this.table.copyTo(var1.getID(), var2.table);
            } finally {
               this.lock.readLock().unlock();
            }
         } finally {
            var2.lock.writeLock().unlock();
         }
      }
   }

   private void deleteResourcesForEntity(IntegrationEntity var1) {
      FileResourceManager.EntityRoot var2 = this.roots.remove(var1);
      if (var2 != null) {
         var2.deleteAll();
      }
   }

   public void importResource(IntegrationEntityId var1, String var2, String var3) {
      this.lock.writeLock().lock();

      try {
         this.table.write(var1, var2, var3);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   public void dumpResourcesToConsole() {
      this.lock.readLock().lock();

      try {
         this.table.dumpToConsole();
      } finally {
         this.lock.readLock().unlock();
      }
   }

   private class EntityRoot {
      private final IntegrationEntity entity;
      private final Map<String, SQLiteFileResource> resources = Maps.newHashMap();

      public EntityRoot(IntegrationEntity var2) {
         this.entity = var2;
      }

      public SQLiteFileResource get(String var1) {
         SQLiteFileResource var2 = this.resources.get(var1);
         if (var2 == null) {
            var2 = new SQLiteFileResource(FileResourceManager.this.table, FileResourceManager.this.lock, this.entity, var1);
            this.resources.put(var1, var2);
         }

         return var2;
      }

      public Iterable<SQLiteFileResource> getAll() {
         return this.resources.values();
      }

      public void deleteAll() {
         for (SQLiteFileResource var2 : this.resources.values()) {
            var2.delete();
         }

         this.resources.clear();
      }
   }
}
