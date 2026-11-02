package org.etlforge.application.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;

public abstract class AbstractTextFileCache implements TextFileCache {
   private final String cacheId;
   private final File parentDir;
   private final boolean deleteOnExit;
   private final Set<Object> storedObjects;

   protected AbstractTextFileCache(String var1, File var2, boolean var3) {
      this.cacheId = var1;
      this.parentDir = var2;
      this.deleteOnExit = var3;
      this.storedObjects = Sets.newHashSet();
   }

   @Override
   public String getID() {
      return this.cacheId;
   }

   public void ensureParentFolderExists() throws DataCacheException {
      if (!this.parentDir.exists() && !this.parentDir.mkdirs()) {
         throw new DataCacheException("Failed to create the directory " + this.parentDir);
      }

      if (!this.parentDir.isDirectory()) {
         throw new DataCacheException("The file " + this.parentDir + " is not a directory.");
      }
   }

   @Override
   public final void store(Object var1, String var2, Charset var3) throws DataCacheException {
      try {
         File var4 = this.getFile(var1);
         if (this.deleteOnExit) {
            var4.deleteOnExit();
         }

         Files.write(var2, var4, var3 == null ? Charset.defaultCharset() : var3);
         this.storedObjects.add(var1);
      } catch (IOException var5) {
         throw new DataCacheException(var5);
      }
   }

   @Override
   public final void store(Object var1, byte[] var2) throws DataCacheException {
      try {
         File var3 = this.getFile(var1);
         if (this.deleteOnExit) {
            var3.deleteOnExit();
         }

         Files.write(var2, var3);
         this.storedObjects.add(var1);
      } catch (IOException var4) {
         throw new DataCacheException(var4);
      }
   }

   @Override
   public final boolean contains(Object var1) {
      return this.getFile(var1).exists();
   }

   @Override
   public final String retrieve(Object var1, Charset var2) throws DataCacheException {
      if (this.contains(var1)) {
         try {
            return this.load(var1, var2);
         } catch (IOException var4) {
            throw new DataCacheException(var4);
         }
      } else {
         return null;
      }
   }

   @Override
   public final byte[] retrieveAsBytes(Object var1) throws DataCacheException {
      if (this.contains(var1)) {
         try {
            return this.loadAsBytes(var1);
         } catch (IOException var3) {
            throw new DataCacheException(var3);
         }
      } else {
         return null;
      }
   }

   @Override
   public final boolean delete(Object var1) {
      File var2 = this.getFile(var1);
      this.storedObjects.remove(var1);
      return !var2.exists() || var2.delete();
   }

   @Override
   public final void deleteAll() {
      for (Object var2 : Lists.newArrayList(this.storedObjects)) {
         this.delete(var2);
      }
   }

   @Override
   public final Object[] getCachedObjectIds() {
      return this.storedObjects.toArray();
   }

   protected abstract String getFileName(Object var1);

   private String load(Object var1, Charset var2) throws IOException {
      File var3 = this.getFile(var1);
      return Files.toString(var3, var2 == null ? Charset.defaultCharset() : var2);
   }

   private byte[] loadAsBytes(Object var1) throws IOException {
      return Files.toByteArray(this.getFile(var1));
   }

   public File getFile(Object var1) {
      return new File(this.parentDir, this.getFileName(var1));
   }
}
