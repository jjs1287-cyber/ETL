package org.etlforge.integration.data.project;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ItemRevivalOrder implements Comparator<IntegrationEntity>, Serializable {
   private final Comparator<IntegrationEntity> impl = Collections.reverseOrder(new ItemDeleteOrder());

   public int compare(IntegrationEntity var1, IntegrationEntity var2) {
      return this.impl.compare(var1, var2);
   }
}
