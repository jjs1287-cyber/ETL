package org.etlforge.integration.client.structure.cache;

import com.google.common.base.Optional;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;
import org.etlforge.io.Folder;
import org.etlforge.io.KongaIoUtils;

final class FileBasedMappingTreeStructureCache implements MappingTreeStructureCache {
   private final Folder folder;
   private final String prefix;

   public FileBasedMappingTreeStructureCache(Folder var1, String var2) {
      this.folder = var1;
      this.prefix = var2;
      if (!this.folder.createOnDisk()) {
         ApplicationLog.getMainLogger().warning("Failed to create a FileBasedMappingTreeStructureCache at " + var1.getAbsolutePath());
      }
   }

   @Override
   public void delete(Object var1) {
      File var2 = this.getCachedFile(var1);
      if (var2 != null) {
         var2.delete();
      }
   }

   @Override
   public Writer getWriter(Object var1) throws IOException {
      this.deleteOldFormatFileIfPresent(var1);
      GZIPOutputStream var2 = null;
      boolean var3 = false;

      try {
         String var4 = var1 + this.prefix + ".gz";
         var2 = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(this.folder.getFile(var4))));
         var3 = true;
         return new OutputStreamWriter(var2);
      } finally {
         if (!var3) {
            KongaIoUtils.close(var2);
         }
      }
   }

   private void deleteOldFormatFileIfPresent(Object var1) {
      String var2 = var1 + this.prefix + ".jtr";
      File var3 = this.folder.getFile(var2);
      if (var3.exists()) {
         var3.delete();
      }
   }

   @Override
   public Optional<MappingTreeStructure> retrieve(Object var1) {
      MappingTreeStructure var2 = null;
      File var3 = this.getCachedFile(var1);
      if (var3 != null && var3.canRead()) {
         var2 = MappingTreeStructureFactory.fromFile(var3);
      }

      return Optional.fromNullable(var2);
   }

   private File getCachedFile(Object var1) {
      for (String var3 : Arrays.asList("gz", "jtr")) {
         String var4 = var1 + this.prefix + "." + var3;
         File var5 = this.folder.getFile(var4);
         if (var5.exists()) {
            return var5;
         }
      }

      return null;
   }

   @Override
   public void copy(Object var1, Object var2) throws DataCacheException {
      File var3 = this.getCachedFile(var1);
      if (var3 != null && var3.canRead()) {
         String var4 = getFileExtension(var3);
         File var5 = this.folder.getFile(var2 + this.prefix + var4);

         try {
            FileUtils.copyFile(var3, var5);
         } catch (IOException var7) {
            throw new DataCacheException(var7);
         }
      }
   }

   private static String getFileExtension(File var0) {
      String var1 = FilenameUtils.getExtension(var0.getName()).toLowerCase();
      if (isDotMissing(var1)) {
         var1 = "." + var1;
      }

      return var1;
   }

   private static boolean isDotMissing(String var0) {
      return StringUtils.isNotEmpty(var0) && !var0.startsWith(".");
   }
}
