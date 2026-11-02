package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.integration.data.location.LocalFileLocation;

public interface LocalFileLocationCache extends DataLocationCache<LocalFileLocation> {
   String ID = LocalFileLocationCache.class.getName();

   String[] getFolderPaths();

   boolean removeFolderPath(String var1);
}
