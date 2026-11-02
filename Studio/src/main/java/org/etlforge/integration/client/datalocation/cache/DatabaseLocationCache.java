package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.integration.data.location.DatabaseLocation;

public interface DatabaseLocationCache extends DataLocationCache<DatabaseLocation>, LoginNameCache {
   String ID = DatabaseLocationCache.class.getName();

   String[] getServerNames();

   boolean removeServerName(String var1);

   String[] getDatabaseNames();

   boolean removeDatabaseName(String var1);

   @Override
   String[] getLoginNames();
}
