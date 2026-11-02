package org.jitterbit.integration.client.entity.settings.sqlite;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.SerializationUtils;
import org.jitterbit.integration.client.entity.settings.FileResource;
import org.jitterbit.integration.data.entity.IntegrationEntity;
import org.jitterbit.lang.KongaStringUtils;

final class SQLiteFileResource implements FileResource {
   private final SQLiteFileResourceWorker impl;
   private final ReadWriteLock lock;
   private volatile FileResource.LifeTime lifeTime;

   public SQLiteFileResource(FileResourcesTable var1, ReadWriteLock var2, IntegrationEntity var3, String var4) {
      this.lock = var2;
      this.impl = new SQLiteFileResourceWorker(var1, var3, var4);
      this.lifeTime = FileResource.LifeTime.ENTITY;
   }

   @Override
   public boolean exists() {
      this.lock.readLock().lock();

      try {
         return this.impl.exists();
      } finally {
         this.lock.readLock().unlock();
      }
   }

   @Override
   public String read() {
      this.lock.readLock().lock();

      try {
         return this.impl.read();
      } finally {
         this.lock.readLock().unlock();
      }
   }

   @Override
   public void write(String var1) {
      this.lock.writeLock().lock();

      try {
         this.impl.write(var1);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   @Override
   public void serialize(Serializable var1) throws IOException {
      byte[] var2 = SerializationUtils.serialize(var1);
      this.write(KongaStringUtils.toHexString(var2));
   }

   @Override
   public <T> T deserialize(Class<T> var1) throws IOException, ClassNotFoundException {
      String var2 = this.read();
      if (var2 == null) {
         return null;
      }

      byte[] var3 = fromHexString(var2);
      Object var4 = SerializationUtils.deserialize(var3);
      return (T)var1.cast(var4);
   }

   private static byte[] fromHexString(String var0) {
      try {
         return Hex.decodeHex(var0.toCharArray());
      } catch (DecoderException var2) {
         throw new RuntimeException(var2);
      }
   }

   @Override
   public void delete() {
      this.lock.writeLock().lock();

      try {
         this.impl.delete();
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   @Override
   public void setLifeTime(FileResource.LifeTime var1) {
      this.lifeTime = var1;
   }

   @Override
   public FileResource.LifeTime getLifeTime() {
      return this.lifeTime;
   }
}
