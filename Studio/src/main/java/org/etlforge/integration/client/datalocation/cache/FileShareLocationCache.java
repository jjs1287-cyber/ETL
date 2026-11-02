package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.integration.data.location.FileShareLocation;

public interface FileShareLocationCache extends DataLocationCache<FileShareLocation>, LoginNameCache {
   String ID = FileShareLocationCache.class.getName();

   String[] getPaths();

   boolean removePath(String var1);

   @Override
   String[] getLoginNames();
}
