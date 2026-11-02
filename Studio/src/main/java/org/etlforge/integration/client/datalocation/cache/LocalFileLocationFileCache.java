package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.io.Folder;

final class LocalFileLocationFileCache extends AbstractDataLocationCache<LocalFileLocation> implements LocalFileLocationCache {
   private final LocalFileLocationFileCache.PathParticle paths;

   public LocalFileLocationFileCache(File var1) {
      super(ID, LocalFileLocation.class);
      Preconditions.checkArgument(var1.isDirectory(), "location must be a directory");
      Folder var2 = this.createParent(var1);
      this.paths = new LocalFileLocationFileCache.PathParticle(var2);
      this.addStringCache(this.paths);
   }

   @Override
   public String[] getFolderPaths() {
      return this.paths.getValues();
   }

   @Override
   public boolean removeFolderPath(String var1) {
      return this.paths.remove(var1);
   }

   private static class PathParticle extends StringCacheParticle<LocalFileLocation> {
      public PathParticle(Folder var1) {
         super("LocalFileLocationFolder", var1.getFile("folder_paths.dat"), 100, true);
      }

      protected String getValueFromObject(LocalFileLocation var1) {
         return var1.getFolderPath();
      }
   }
}
