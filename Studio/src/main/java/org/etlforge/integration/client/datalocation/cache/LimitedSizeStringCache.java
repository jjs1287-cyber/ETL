package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.io.KongaIoUtils;

final class LimitedSizeStringCache {
   private final String name;
   private final File file;
   private final boolean caseSensitive;
   private final LimitedSizeStringCache.StringPool stringPool;
   private final Object lock = new Object();

   public LimitedSizeStringCache(String var1, File var2, int var3, boolean var4) {
      this.name = var1;
      this.file = var2;
      this.caseSensitive = var4;
      this.stringPool = new LimitedSizeStringCache.StringPool(var3, var4);
      this.load();
   }

   public String[] getValues() {
      synchronized (this.lock) {
         return this.stringPool.get();
      }
   }

   public void store(String var1) {
      this.store(Collections.singletonList(var1));
   }

   public void store(Collection<String> var1) {
      synchronized (this.lock) {
         for (String var4 : var1) {
            this.stringPool.add(var4);
         }
      }

      this.writeToDisk();
   }

   public boolean remove(String var1) {
      boolean var2 = false;
      synchronized (this.lock) {
         var2 = this.stringPool.remove(var1);
      }

      if (var2) {
         this.writeToDisk();
      }

      return var2;
   }

   private void load() {
      if (this.file.exists()) {
         synchronized (this.lock) {
            try {
               for (String var3 : new LimitedSizeStringCache.FileLoader().loadValues()) {
                  this.stringPool.add(var3);
               }
            } catch (IOException var5) {
               ErrorLog.log(LimitedSizeStringCache.class, "Failed to load the cache " + this.name + " from disk.", var5);
            }
         }
      }
   }

   private void writeToDisk() {
      LimitedSizeStringCache.FileSaver var1 = new LimitedSizeStringCache.FileSaver();
      Thread var2 = new Thread(var1);
      var2.setPriority(2);
      var2.start();
   }

   private class FileLoader {
      private final Set<String> pool = Sets.newHashSet();

      public FileLoader() {
      }

      public Iterable<String> loadValues() throws IOException {
         ArrayList var1 = Lists.newArrayList();

         for (String var5 : KongaIoUtils.readLines(LimitedSizeStringCache.this.file, Charsets.UTF_8)) {
            if (this.isUnique(var5)) {
               var1.add(var5);
               this.pool.add(LimitedSizeStringCache.this.caseSensitive ? var5 : var5.toLowerCase());
            }
         }

         return var1;
      }

      private boolean isUnique(String var1) {
         return LimitedSizeStringCache.this.caseSensitive ? !this.pool.contains(var1) : !this.pool.contains(var1.toLowerCase());
      }
   }

   private class FileSaver implements Runnable {
      private FileSaver() {
      }

      @Override
      public void run() {
         synchronized (LimitedSizeStringCache.this.lock) {
            try {
               String[] var2 = LimitedSizeStringCache.this.stringPool.get();
               KongaIoUtils.createAndWrite(LimitedSizeStringCache.this.file, var2, Charsets.UTF_8, false);
            } catch (IOException var4) {
               ErrorLog.log(LimitedSizeStringCache.class, "Failed to write values to disk", var4);
            }
         }
      }
   }

   private static class StringPool {
      private final int size;
      private final boolean caseSensitive;
      private final LinkedHashMap<String, String> strings;

      public StringPool(int var1, boolean var2) {
         this.size = var1;
         this.caseSensitive = var2;
         this.strings = Maps.newLinkedHashMap();
      }

      public void add(String var1) {
         if (StringUtils.isNotEmpty(var1)) {
            String var2 = this.caseSensitive ? var1 : var1.toLowerCase();
            this.strings.put(var2, var1);
         }
      }

      public String[] get() {
         ArrayList var1 = Lists.newArrayList(this.strings.values());
         Collections.reverse(var1);
         String[] var2 = (String[]) var1.toArray(new String[var1.size()]);
         if (var2.length <= this.size) {
            return var2;
         }

         String[] var3 = new String[this.size];
         System.arraycopy(var2, 0, var3, 0, this.size);
         return var3;
      }

      public boolean remove(String var1) {
         String var2 = this.caseSensitive ? var1 : var1.toLowerCase();
         return this.strings.remove(var2) != null;
      }
   }
}
