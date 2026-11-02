package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.integration.data.location.LdapLocation;

public interface LdapLocationCache extends DataLocationCache<LdapLocation>, LoginNameCache {
   String ID = LdapLocationCache.class.getName();

   String[] getServerNames();

   boolean removeServerName(String var1);

   @Override
   String[] getLoginNames();

   String[] getBaseNames();

   boolean removeBaseName(String var1);
}
