package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FilenameUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.io.Folder;
import org.etlforge.util.KongaSerializationUtils;

public final class ConnectorObjectFileCache implements ConnectorObjectCache {
   private static final String CACHE_EXT = ".cache";
   private static final String TIME_EXT = ".time";
   private final Folder root;
   private final Object lock = new Object();
   private final long ageLimitInMillis;

   public ConnectorObjectFileCache(Folder var1) {
      this.root = Preconditions.checkNotNull(var1, "root");
      this.ageLimitInMillis = -1L;
   }

   public ConnectorObjectFileCache(Folder var1, long var2) {
      this(var1, var2, TimeUnit.DAYS);
   }

   public ConnectorObjectFileCache(Folder var1, long var2, TimeUnit var4) {
      this.root = Preconditions.checkNotNull(var1, "root");
      this.ageLimitInMillis = var4.toMillis(var2);
   }

   @Override
   public List<ConnectorObject> getObjects(ConnectorObjectCache.Key var1) {
      File var2 = this.getCacheFile(var1);
      synchronized (this.lock) {
         if (var2.exists()) {
            List var10000;
            try {
               ConnectorObjectFileCache.CachedValue var4 = KongaSerializationUtils.deserializeFromCompressedFile(
                  var2, ConnectorObjectFileCache.CachedValue.class
               );
               this.markAsAccessed(var1);
               var10000 = var4.asList();
            } catch (Exception var6) {
               ErrorLog.internal(this.getClass(), null, var6);
               return ImmutableList.of();
            }

            return var10000;
         }
      }

      return ImmutableList.of();
   }

   private File getCacheFile(ConnectorObjectCache.Key var1) {
      return this.root.getFile(var1.toString() + ".cache");
   }

   private void markAsAccessed(ConnectorObjectCache.Key var1) {
      try {
         File var2 = this.getTimeFile(var1);
         Files.write("" + System.currentTimeMillis(), var2, Charsets.US_ASCII);
      } catch (IOException var3) {
         ErrorLog.internal(this.getClass(), null, var3);
      }
   }

   private File getTimeFile(ConnectorObjectCache.Key var1) {
      return this.root.getFile(var1.toString() + ".time");
   }

   @Override
   public void storeObjects(ConnectorObjectCache.Key var1, Iterable<? extends ConnectorObject> var2) {
      this.root.createOnDisk();
      File var3 = this.getCacheFile(var1);
      ArrayList var4 = Lists.newArrayList(var2);
      synchronized (this.lock) {
         if (var4.isEmpty()) {
            this.remove(var1);
         } else {
            try {
               KongaSerializationUtils.serializeToCompressedFile(new ConnectorObjectFileCache.CachedValue(var4), var3);
               this.markAsAccessed(var1);
            } catch (IOException var8) {
               ErrorLog.internal(this.getClass(), null, var8);
            }
         }
      }
   }

   @Override
   public void remove(ConnectorObjectCache.Key var1) {
      File var2 = this.getCacheFile(var1);
      File var3 = this.getTimeFile(var1);
      synchronized (this.lock) {
         var2.delete();
         var3.delete();
      }
   }

   @Override
   public ImmutableList<String> evictOldEntries() {
      if (this.ageLimitInMillis < 0L) {
         return ImmutableList.of();
      }

      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.ageLimitInMillis;
      FileFilter var5 = new FileFilter() {
         @Override
         public boolean accept(File var1) {
            return var1.getName().endsWith(".time");
         }
      };
      Builder var6 = ImmutableList.builder();
      synchronized (this.lock) {
         for (File var11 : this.root.listFiles(var5)) {
            if (this.isTooOld(var11, var3)) {
               this.deleteFiles(var11);
               String var12 = getKeyName(var11);
               var6.add(var12);
            }
         }
      }

      return var6.build();
   }

   private static String getKeyName(File var0) {
      return FilenameUtils.getBaseName(var0.getName());
   }

   private boolean isTooOld(File var1, long var2) {
      try {
         String var4 = Files.readFirstLine(var1, Charsets.US_ASCII);
         long var5 = Long.parseLong(var4);
         return var5 < var2;
      } catch (Exception var7) {
         ErrorLog.internal(this.getClass(), null, var7);
         return true;
      }
   }

   private void deleteFiles(File var1) {
      var1.delete();
      String var2 = getKeyName(var1);
      File var3 = this.root.getFile(var2 + ".cache");
      var3.delete();
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("path", this.root.getAbsolutePath()).toString();
   }

   private static class CachedValue implements Serializable {
      private final ArrayList<ConnectorObject> objects;

      public CachedValue(List<ConnectorObject> var1) {
         this.objects = Lists.newArrayList(var1);
      }

      public List<ConnectorObject> asList() {
         return ImmutableList.copyOf(this.objects);
      }
   }
}
