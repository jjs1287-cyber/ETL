package org.etlforge.integration.client.ui.interchange.location.ftp;

import java.util.Arrays;
import org.etlforge.integration.client.datalocation.cache.FtpLocationCache;
import org.etlforge.ui.text.contentassist.StringValueHistory;

class CacheBasedHostHistory implements StringValueHistory {
   private final FtpLocationCache cache;

   public CacheBasedHostHistory(FtpLocationCache var1) {
      assert var1 != null : "cache cannot be null";
      this.cache = var1;
   }

   @Override
   public String[] values() {
      String[] var1 = this.cache.getHosts();
      Arrays.sort(var1, String.CASE_INSENSITIVE_ORDER);
      return var1;
   }

   @Override
   public boolean delete(String var1) {
      return this.cache.removeHost(var1);
   }
}
