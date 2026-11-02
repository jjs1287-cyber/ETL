package org.etlforge.integration.client.datalocation.cache;

import org.etlforge.integration.data.location.TemporaryStorageLocation;

public interface TemporaryStorageLocationCache extends DataLocationCache<TemporaryStorageLocation> {
   String ID = TemporaryStorageLocation.class.getName();

   String[] getPaths();

   boolean removePath(String var1);
}
