package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.io.Folder;

final class HttpLocationFileCache extends AbstractDataLocationCache<HttpLocation> implements HttpLocationCache {
   private static final int SIZE = 100;
   private final HttpLocationFileCache.UrlCache urlCache;
   private final LoginNameCacheParticle<HttpLocation> loginCache;

   HttpLocationFileCache(File var1) {
      super(ID, HttpLocation.class);
      Preconditions.checkArgument(var1.isDirectory(), "location must be a directory");
      Folder var2 = this.createParent(var1);
      this.urlCache = new HttpLocationFileCache.UrlCache(var2);
      this.loginCache = new LoginNameCacheParticle<>("HttpLocationLogin", var2, 100);
      this.addStringCache(this.urlCache);
      this.addStringCache(this.loginCache);
   }

   @Override
   public String[] getUrls() {
      return this.urlCache.getValues();
   }

   @Override
   public boolean removeUrl(String var1) {
      return this.urlCache.remove(var1);
   }

   @Override
   public String[] getLoginNames() {
      return this.loginCache.getValues();
   }

   @Override
   public boolean removeLoginName(String var1) {
      return this.loginCache.remove(var1);
   }

   private static class UrlCache extends StringCacheParticle<HttpLocation> {
      public UrlCache(Folder var1) {
         super("HttpLocationUrl", var1.getFile("urls.dat"), 100, false);
      }

      protected String getValueFromObject(HttpLocation var1) {
         return var1.getUrl();
      }
   }
}
