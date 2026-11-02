package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.io.Folder;

final class FileShareLocationFileCache extends AbstractDataLocationCache<FileShareLocation> implements FileShareLocationCache {
   private static final int SIZE = 100;
   private final FileShareLocationFileCache.PathParticle paths;
   private final LoginNameCacheParticle<FileShareLocation> logins;

   public FileShareLocationFileCache(File var1) {
      super(ID, FileShareLocation.class);
      Preconditions.checkArgument(var1.isDirectory(), "location must be a directory");
      Folder var2 = this.createParent(var1);
      this.paths = new FileShareLocationFileCache.PathParticle(var2);
      this.logins = new LoginNameCacheParticle<>("FileShareLocationLogin", var2, 100);
      this.addStringCache(this.paths);
      this.addStringCache(this.logins);
   }

   @Override
   public String[] getPaths() {
      return this.paths.getValues();
   }

   @Override
   public boolean removePath(String var1) {
      return this.paths.remove(var1);
   }

   @Override
   public String[] getLoginNames() {
      return this.logins.getValues();
   }

   @Override
   public boolean removeLoginName(String var1) {
      return this.logins.remove(var1);
   }

   private static class PathParticle extends StringCacheParticle<FileShareLocation> {
      public PathParticle(Folder var1) {
         super("FileShareLocationPath", var1.getFile("paths.dat"), 100, true);
      }

      protected String getValueFromObject(FileShareLocation var1) {
         return var1.getPath();
      }
   }
}
