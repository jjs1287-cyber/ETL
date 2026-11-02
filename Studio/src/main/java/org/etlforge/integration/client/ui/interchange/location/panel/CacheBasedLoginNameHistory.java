package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.datalocation.cache.LoginNameCache;
import org.etlforge.ui.text.contentassist.StringValueHistory;

public final class CacheBasedLoginNameHistory implements StringValueHistory {
   private final LoginNameCache cache;

   public CacheBasedLoginNameHistory(LoginNameCache var1) {
      this.cache = Preconditions.checkNotNull(var1);
   }

   @Override
   public String[] values() {
      return this.cache.getLoginNames();
   }

   @Override
   public boolean delete(String var1) {
      return this.cache.removeLoginName(var1);
   }
}
