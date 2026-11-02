package org.etlforge.integration.client.ui.interchange.location.fileshare;

import java.util.Arrays;
import java.util.Collection;
import org.etlforge.integration.client.datalocation.cache.FileShareLocationCache;
import org.etlforge.integration.client.ui.interchange.location.panel.PathFieldSupport;

class CacheBasedPathProvider implements PathFieldSupport.PathProvider {
   private final FileShareLocationCache cache;
   private Collection<String> paths;

   CacheBasedPathProvider(FileShareLocationCache var1) {
      assert var1 != null : "cache cannot be null";
      this.cache = var1;
   }

   @Override
   public Collection<String> getPaths() {
      if (this.paths == null) {
         this.paths = this.loadPathsFromCache();
      }

      return this.paths;
   }

   @Override
   public String getPrefix() {
      return "\\\\";
   }

   @Override
   public String getSeparator() {
      return "\\";
   }

   @Override
   public void refresh() {
      this.paths = null;
   }

   private Collection<String> loadPathsFromCache() {
      return Arrays.asList(this.cache.getPaths());
   }
}
