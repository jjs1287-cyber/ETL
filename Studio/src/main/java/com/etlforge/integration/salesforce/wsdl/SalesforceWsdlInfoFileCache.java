package com.etlforge.integration.salesforce.wsdl;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.util.KongaSerializationUtils;

public final class SalesforceWsdlInfoFileCache implements SalesforceWsdlInfoCache {
   private final File directory;
   private final ReadWriteLock lock;

   public SalesforceWsdlInfoFileCache(File var1) {
      this.directory = var1;
      if (!var1.exists() && !var1.mkdirs()) {
         ErrorLog.log(
            this.getClass(), "Failed to create the salesforce WSDL cache.", new Exception("Could not create the cache directory " + var1.getAbsolutePath())
         );
      }

      this.lock = new ReentrantReadWriteLock();
   }

   @Override
   public String getID() {
      return ID;
   }

   @Override
   public SalesforceWsdlInfo getCachedInfo(String var1, InputStream var2) {
      try {
         if (this.lock.readLock().tryLock(1L, TimeUnit.SECONDS)) {
            try {
               return this.readFromCache(var1, var2);
            } finally {
               this.lock.readLock().unlock();
            }
         }
      } catch (InterruptedException var8) {
         Thread.currentThread().interrupt();
      }

      return null;
   }

   private SalesforceWsdlInfo readFromCache(String var1, InputStream var2) {
      try {
         File var3 = this.getCachedFile(var1, var2);
         if (var3.canRead()) {
            return KongaSerializationUtils.deserializeFromFile(var3, SalesforceWsdlInfo.class);
         }
      } catch (Exception var4) {
         ErrorLog.internal(this.getClass(), "An error occurred while trying to read from the salesforce WSDL info cache.", var4);
      }

      return null;
   }

   private File getCachedFile(String var1, InputStream var2) throws Exception {
      try {
         byte[] var3 = IOUtils.toByteArray(var2);
         String var4 = DigestUtils.md5Hex(var3);
         String var5 = var1 + "." + var4;
         return new File(this.directory, var5);
      } finally {
         KongaIoUtils.close(var2);
      }
   }

   @Override
   public void storeInfo(String var1, InputStream var2, SalesforceWsdlInfo var3) {
      try {
         this.lock.writeLock().lockInterruptibly();
         this.writeToCache(var1, var2, var3);
      } catch (InterruptedException var8) {
         Thread.currentThread().interrupt();
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   private void writeToCache(String var1, InputStream var2, SalesforceWsdlInfo var3) {
      boolean var4 = false;
      File var5 = null;

      try {
         var5 = this.getCachedFile(var1, var2);
         var4 = var5.exists();
         KongaSerializationUtils.serializeToFile(var3, var5);
      } catch (Exception var7) {
         ErrorLog.log(this.getClass(), "An error occurred when trying to store salesforce WSDL info in the client cache.", var7);
         if (var4 && var5 != null && !var5.delete()) {
         }
      }
   }
}
