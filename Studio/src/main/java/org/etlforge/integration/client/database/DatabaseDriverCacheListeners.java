package org.etlforge.integration.client.database;

import java.util.Collection;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class DatabaseDriverCacheListeners extends AbstractListenerManager<DatabaseDriverCacheListener> implements ListenerManager<DatabaseDriverCacheListener> {
   protected DatabaseDriverCacheListener createNotifier() {
      return new DatabaseDriverCacheListener() {
         @Override
         public void cacheWasUpdated(Collection<? extends DriverDescriptor> var1) {
            for (DatabaseDriverCacheListener var3 : DatabaseDriverCacheListeners.this.getListeners()) {
               var3.cacheWasUpdated(var1);
            }
         }
      };
   }
}
