package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.io.Folder;

final class TemporaryStorageLocationFileCache extends AbstractDataLocationCache<TemporaryStorageLocation> implements TemporaryStorageLocationCache {
   private static final int SIZE = 100;
   private final TemporaryStorageLocationFileCache.PathParticle paths;

   public TemporaryStorageLocationFileCache(File var1) {
      super(ID, TemporaryStorageLocation.class);
      Preconditions.checkArgument(var1.isDirectory(), "location must be a directory");
      Folder var2 = this.createParent(var1);
      this.paths = new TemporaryStorageLocationFileCache.PathParticle(var2);
      this.addStringCache(this.paths);
   }

   @Override
   public String[] getPaths() {
      return this.paths.getValues();
   }

   @Override
   public boolean removePath(String var1) {
      return this.paths.remove(var1);
   }

   private static class PathParticle extends StringCacheParticle<TemporaryStorageLocation> {
      public PathParticle(Folder var1) {
         super("TemporaryStorageLocationPath", var1.getFile("paths.dat"), 100, true);
      }

      protected String getValueFromObject(TemporaryStorageLocation var1) {
         return var1.getPath();
      }
   }
}
