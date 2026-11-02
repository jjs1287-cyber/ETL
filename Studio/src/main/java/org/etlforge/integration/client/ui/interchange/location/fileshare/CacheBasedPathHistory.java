package org.etlforge.integration.client.ui.interchange.location.fileshare;

import java.util.Arrays;
import org.etlforge.integration.client.datalocation.cache.FileShareLocationCache;
import org.etlforge.ui.text.contentassist.StringValueHistory;

class CacheBasedPathHistory implements StringValueHistory {
   private final FileShareLocationCache cache;

   CacheBasedPathHistory(FileShareLocationCache var1) {
      this.cache = var1;
   }

   @Override
   public boolean delete(String var1) {
      return this.cache.removePath(var1);
   }

   @Override
   public String[] values() {
      String[] var1 = this.cache.getPaths();
      Arrays.sort(var1, String.CASE_INSENSITIVE_ORDER);
      return var1;
   }
}
