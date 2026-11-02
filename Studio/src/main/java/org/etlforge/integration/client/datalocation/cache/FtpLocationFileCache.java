package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.io.Folder;

final class FtpLocationFileCache extends AbstractDataLocationCache<FtpLocation> implements FtpLocationCache {
   private static final int SIZE = 100;
   private final FtpLocationFileCache.HostCache hostCache;
   private final FtpLocationFileCache.PathCache pathCache;
   private final LoginNameCacheParticle<FtpLocation> loginCache;

   FtpLocationFileCache(File var1) {
      super(ID, FtpLocation.class);
      Preconditions.checkArgument(var1.isDirectory(), "location must be a directory");
      Folder var2 = this.createParent(var1);
      this.hostCache = new FtpLocationFileCache.HostCache(var2);
      this.pathCache = new FtpLocationFileCache.PathCache(var2);
      this.loginCache = new LoginNameCacheParticle<>("FtpLocationLogin", var2, 100);
      this.addStringCache(this.hostCache);
      this.addStringCache(this.pathCache);
      this.addStringCache(this.loginCache);
   }

   @Override
   public String[] getHosts() {
      return this.hostCache.getValues();
   }

   @Override
   public boolean removeHost(String var1) {
      return this.hostCache.remove(var1);
   }

   @Override
   public String[] getPaths() {
      return this.pathCache.getValues();
   }

   @Override
   public boolean removePath(String var1) {
      return this.pathCache.remove(var1);
   }

   @Override
   public String[] getLoginNames() {
      return this.loginCache.getValues();
   }

   @Override
   public boolean removeLoginName(String var1) {
      return this.loginCache.remove(var1);
   }

   private static class HostCache extends StringCacheParticle<FtpLocation> {
      public HostCache(Folder var1) {
         super("FtpLocationHost", var1.getFile("hosts.dat"), 100, false);
      }

      protected String getValueFromObject(FtpLocation var1) {
         return var1.getHost();
      }
   }

   private static class PathCache extends StringCacheParticle<FtpLocation> {
      public PathCache(Folder var1) {
         super("FtpLocationPath", var1.getFile("paths.dat"), 100, true);
      }

      protected String getValueFromObject(FtpLocation var1) {
         return var1.getHostPath();
      }
   }
}
