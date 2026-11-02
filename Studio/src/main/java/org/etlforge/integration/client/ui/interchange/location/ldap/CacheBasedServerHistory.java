package org.etlforge.integration.client.ui.interchange.location.ldap;

import org.etlforge.integration.client.datalocation.cache.LdapLocationCache;
import org.etlforge.ui.text.contentassist.StringValueHistory;

class CacheBasedServerHistory implements StringValueHistory {
   private final LdapLocationCache cache;

   CacheBasedServerHistory(LdapLocationCache var1) {
      this.cache = var1;
   }

   @Override
   public String[] values() {
      return this.cache.getServerNames();
   }

   @Override
   public boolean delete(String var1) {
      return this.cache.removeServerName(var1);
   }
}
