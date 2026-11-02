package org.etlforge.integration.client.ui.connector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public final class InMemoryConnectorObjectCache implements ConnectorObjectCache {
   private final ConcurrentMap<ConnectorObjectCache.Key, List<ConnectorObject>> map = Maps.newConcurrentMap();

   @Override
   public List<ConnectorObject> getObjects(ConnectorObjectCache.Key var1) {
      List var2 = this.map.get(var1);
      return var2 == null ? ImmutableList.of() : var2;
   }

   @Override
   public void storeObjects(ConnectorObjectCache.Key var1, Iterable<? extends ConnectorObject> var2) {
      this.map.put(var1, ImmutableList.copyOf(var2));
   }

   @Override
   public void remove(ConnectorObjectCache.Key var1) {
      this.map.remove(var1);
   }

   @Override
   public ImmutableList<String> evictOldEntries() {
      return ImmutableList.of();
   }
}
