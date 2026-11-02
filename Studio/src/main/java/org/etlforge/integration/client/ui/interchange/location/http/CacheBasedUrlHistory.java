package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.integration.client.datalocation.cache.HttpLocationCache;
import org.etlforge.ui.text.contentassist.StringValueHistory;

class CacheBasedUrlHistory implements StringValueHistory {
   private final HttpLocationCache cache;

   CacheBasedUrlHistory(HttpLocationCache var1) {
      if (var1 == null) {
         throw new NullPointerException("cache");
      }

      this.cache = var1;
   }

   @Override
   public String[] values() {
      return this.cache.getUrls();
   }

   @Override
   public boolean delete(String var1) {
      return this.cache.removeUrl(var1);
   }
}
