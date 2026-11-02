package org.jitterbit.integration.client.entity.settings.sqlite;

import java.util.concurrent.locks.ReadWriteLock;
import org.jitterbit.collections.Pair;
import org.jitterbit.integration.client.entity.settings.EntitySettings;
import org.jitterbit.integration.data.entity.EntityType;
import org.jitterbit.integration.data.entity.IntegrationEntity;
import org.jitterbit.integration.data.entity.IntegrationEntityId;
import org.jitterbit.io.Indentation;
import org.jitterbit.util.persist.Persistor;
import org.jitterbit.util.persist.sqlite.PersistorValuesTable;
import org.jitterbit.util.persist.sqlite.PersistorsTable;
import org.jitterbit.util.persist.sqlite.SQLitePersistor;

final class SQLiteEntitySettings implements EntitySettings {
   private final PersistorsTable persistors;
   private final PersistorValuesTable values;
   private final ReadWriteLock lock;

   public SQLiteEntitySettings(PersistorsTable var1, PersistorValuesTable var2, ReadWriteLock var3) {
      this.persistors = var1;
      this.values = var2;
      this.lock = var3;
   }

   @Override
   public Persistor getClassSettings(EntityType var1) {
      return this.getPersistor(var1.name(), true);
   }

   private SQLitePersistor getPersistor(String var1, boolean var2) {
      SQLitePersistor var3 = this.getExistingPersistor(var1);
      if (var3 == null && var2) {
         var3 = new SQLitePersistor(this.persistors, this.values, this.lock, var1);
      }

      return var3;
   }

   private SQLitePersistor getExistingPersistor(String var1) {
      Long var2 = null;
      this.lock.readLock().lock();

      try {
         var2 = this.persistors.getRootPersistorId(var1);
      } finally {
         this.lock.readLock().unlock();
      }

      return var2 == null ? null : new SQLitePersistor(this.persistors, this.values, this.lock, var2, var1);
   }

   @Override
   public Persistor getEntitySettings(IntegrationEntity var1) {
      return this.getEntitySettings(var1.getID(), true);
   }

   public Persistor getEntitySettings(IntegrationEntityId var1) {
      return this.getEntitySettings(var1, true);
   }

   private SQLitePersistor getEntitySettings(IntegrationEntityId var1, boolean var2) {
      return this.getPersistor(var1.toString(), var2);
   }

   @Override
   public void deleteSettings(IntegrationEntity var1) {
      SQLitePersistor var2 = this.getPersistor(var1.getID().toString(), false);
      if (var2 != null) {
         var2.delete();
      }
   }

   @Override
   public void persist() {
   }

   public void exportTo(IntegrationEntity var1, SQLiteEntitySettings var2) {
      this.lock.readLock().lock();

      try {
         SQLitePersistor var3 = this.getEntitySettings(var1.getID(), false);
         if (var3 != null) {
            var2.importEntityRoot(var1, var3);
         }
      } finally {
         this.lock.readLock().unlock();
      }
   }

   private void importEntityRoot(IntegrationEntity var1, SQLitePersistor var2) {
      this.lock.writeLock().lock();

      try {
         this.deleteSettings(var1);
         this.importPersistor(var2, null);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   private void importPersistor(SQLitePersistor var1, Long var2) {
      Long var3 = var2 == null ? this.persistors.insertNewPersistor(var1.getName()) : this.persistors.insertChild(var2, var1.getName());
      if (var3 != null) {
         this.importValues(var1, var3);

         for (Persistor var5 : var1.getAllChildren()) {
            this.importPersistor((SQLitePersistor)var5, var3);
         }
      }
   }

   private void importValues(SQLitePersistor var1, long var2) {
      for (Pair var5 : var1.getAllKeyValues()) {
         this.values.putString(var2, (String)var5.first, (String)var5.second);
      }
   }

   public void dumpToConsole() {
      this.lock.readLock().lock();

      try {
         for (Pair var2 : this.persistors.getRoots()) {
            SQLitePersistor var3 = new SQLitePersistor(this.persistors, this.values, this.lock, (Long)var2.first, (String)var2.second);
            this.dumpToConsole(var3, new Indentation());
         }
      } finally {
         this.lock.readLock().unlock();
      }
   }

   private void dumpToConsole(SQLitePersistor var1, Indentation var2) {
      StringBuilder var3 = new StringBuilder(var2.get());
      var3.append(var1.getName());

      for (Pair var5 : this.values.getAllValues(var1.getId())) {
         var3.append(" [").append((String)var5.first).append(" = ").append((String)var5.second).append("]");
      }

      System.out.println(var3);
      Indentation var7 = var2.increase();

      for (Persistor var6 : var1.getAllChildren()) {
         this.dumpToConsole((SQLitePersistor)var6, var7);
      }
   }
}
