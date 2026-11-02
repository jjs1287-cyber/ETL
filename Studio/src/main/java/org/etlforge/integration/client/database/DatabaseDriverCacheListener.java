package org.etlforge.integration.client.database;

import java.util.Collection;
import org.etlforge.integration.database.driver.DriverDescriptor;

public interface DatabaseDriverCacheListener {
   void cacheWasUpdated(Collection<? extends DriverDescriptor> var1);
}
