package org.etlforge.integration.client.ui.interchange.location.db;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import org.etlforge.integration.client.datalocation.cache.DatabaseLocationCache;
import org.etlforge.ui.text.contentassist.StringValueHistory;

final class CacheBasedServerHistory implements StringValueHistory {
   private final DatabaseLocationCache cache;

   CacheBasedServerHistory(DatabaseLocationCache var1) {
      this.cache = Preconditions.checkNotNull(var1);
   }

   @Override
   public String[] values() {
      String[] var1 = this.cache.getServerNames();
      Arrays.sort(var1, String.CASE_INSENSITIVE_ORDER);
      return var1;
   }

   @Override
   public boolean delete(String var1) {
      return this.cache.removeServerName(var1);
   }
}
