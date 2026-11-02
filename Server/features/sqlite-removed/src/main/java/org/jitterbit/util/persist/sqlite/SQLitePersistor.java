package org.jitterbit.util.persist.sqlite;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.jitterbit.collections.Pair;
import org.jitterbit.util.persist.AbstractPersistor;
import org.jitterbit.util.persist.Persistor;

public final class SQLitePersistor extends AbstractPersistor {
   private final PersistorsTable table;
   private final PersistorValuesTable values;
   private final ReadWriteLock lock;
   private final long id;
   private final String name;

   public static SQLitePersistor newPersistor(Connection var0, String var1) {
      PersistorsTable var2 = new PersistorsTable(var0);
      PersistorValuesTable var3 = new PersistorValuesTable(var0);
      ReentrantReadWriteLock var4 = new ReentrantReadWriteLock();
      long var5 = insertNewPersistor(var2, var1, var4);
      return new SQLitePersistor(var2, var3, var4, var5, var1);
   }

   private static long insertNewPersistor(PersistorsTable var0, String var1, ReadWriteLock var2) {
      Long var3 = null;
      var2.readLock().lock();

      try {
         var3 = var0.insertNewPersistor(var1);
      } finally {
         var2.readLock().unlock();
      }

      if (var3 == null) {
         throw new RuntimeException("Failed to create the requested persistor.");
      } else {
         return var3;
      }
   }

   public SQLitePersistor(PersistorsTable var1, PersistorValuesTable var2, ReadWriteLock var3, String var4) {
      this(var1, var2, var3, insertNewPersistor(var1, var4, var3), var4);
   }

   public SQLitePersistor(PersistorsTable var1, PersistorValuesTable var2, ReadWriteLock var3, long var4, String var6) {
      this.table = var1;
      this.values = var2;
      this.lock = var3;
      this.id = var4;
      this.name = var6;
   }

   public long getId() {
      return this.id;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public Persistor createChild(String var1) {
      this.lock.writeLock().lock();
      Long var2 = null;

      try {
         var2 = this.table.insertChild(this.id, var1);
      } finally {
         this.lock.writeLock().unlock();
      }

      return var2 == null ? null : new SQLitePersistor(this.table, this.values, this.lock, var2, var1);
   }

   @Override
   public Persistor getFirstChild(String var1) {
      this.lock.readLock().lock();
      Long var2 = null;

      try {
         var2 = this.table.getFirstChildId(this.id, var1);
      } finally {
         this.lock.readLock().unlock();
      }

      return var2 == null ? null : new SQLitePersistor(this.table, this.values, this.lock, var2, var1);
   }

   @Override
   public Iterable<Persistor> getChildren(String var1) {
      Object var2 = null;
      this.lock.readLock().lock();

      try {
         var2 = this.table.getChildIds(this.id, var1);
      } finally {
         this.lock.readLock().unlock();
      }

      ArrayList var3 = Lists.newArrayList();

      for (Long var5 : var2) {
         var3.add(new SQLitePersistor(this.table, this.values, this.lock, var5, var1));
      }

      return var3;
   }

   @Override
   public Iterable<Persistor> getAllChildren() {
      Object var1 = null;
      this.lock.readLock().lock();

      try {
         var1 = this.table.getIdAndNameOfAllChildren(this.id);
      } finally {
         this.lock.readLock().unlock();
      }

      ArrayList var2 = Lists.newArrayList();

      for (Pair var4 : var1) {
         var2.add(new SQLitePersistor(this.table, this.values, this.lock, (Long)var4.first, (String)var4.second));
      }

      return var2;
   }

   @Override
   public void removeChildren(String var1) {
      this.lock.writeLock().lock();

      try {
         for (Persistor var3 : this.getChildren(var1)) {
            ((SQLitePersistor)var3).delete();
         }
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   @Override
   public void removeAllChildren() {
      this.lock.writeLock().lock();

      try {
         for (Persistor var2 : this.getAllChildren()) {
            ((SQLitePersistor)var2).delete();
         }
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   @Override
   public void putString(String var1, String var2) {
      this.lock.writeLock().lock();

      try {
         this.values.putString(this.id, var1, var2);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   @Override
   public String getString(String var1) {
      this.lock.readLock().lock();

      try {
         return this.values.getString(this.id, var1);
      } finally {
         this.lock.readLock().unlock();
      }
   }

   public void delete() {
      this.lock.writeLock().lock();

      try {
         this.removeAllChildren();
         this.values.deleteAll(this.id);
         this.table.delete(this.id);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   public List<Pair<String, String>> getAllKeyValues() {
      this.lock.readLock().lock();

      try {
         return this.values.getAllValues(this.id);
      } finally {
         this.lock.readLock().unlock();
      }
   }
}
