package org.etlforge.integration.ldap.client.cache;

import org.etlforge.application.cache.DataCache;

public interface LdapStructureServerCache extends DataCache {
   LdapStructureCache getCache(String var1);
}
