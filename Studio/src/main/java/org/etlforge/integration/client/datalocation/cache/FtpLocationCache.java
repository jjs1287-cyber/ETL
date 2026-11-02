package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.integration.data.location.FtpLocation;

public interface FtpLocationCache extends DataLocationCache<FtpLocation>, LoginNameCache {
   String ID = FtpLocationCache.class.getName();

   String[] getHosts();

   boolean removeHost(String var1);

   String[] getPaths();

   boolean removePath(String var1);

   @Override
   String[] getLoginNames();
}
