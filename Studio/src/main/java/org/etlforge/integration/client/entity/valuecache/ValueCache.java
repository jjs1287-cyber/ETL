package org.etlforge.integration.client.entity.valuecache;

import com.google.common.base.Optional;
import org.etlforge.integration.data.IntegrationData;

public interface ValueCache<T extends IntegrationData> {
   ValueCache EMPTY = new ValueCache() {
      @Override
      public Optional getValues(String var1) {
         return Optional.absent();
      }

      @Override
      public void update(Iterable var1) {
      }
   };

   Optional<History> getValues(String var1);

   void update(Iterable<? extends T> var1);
}
