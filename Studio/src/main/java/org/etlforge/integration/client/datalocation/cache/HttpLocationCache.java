package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.integration.data.location.HttpLocation;

public interface HttpLocationCache extends DataLocationCache<HttpLocation>, LoginNameCache {
   String ID = HttpLocationCache.class.getName();

   String[] getUrls();

   @Override
   String[] getLoginNames();

   boolean removeUrl(String var1);
}
