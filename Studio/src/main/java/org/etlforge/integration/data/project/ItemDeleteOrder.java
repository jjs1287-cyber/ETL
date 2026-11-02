package org.etlforge.integration.data.project;

import java.io.Serializable;
import java.util.Comparator;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ItemDeleteOrder implements Comparator<IntegrationEntity>, Serializable {
   public int compare(IntegrationEntity var1, IntegrationEntity var2) {
      return var1.getEntityType().compareTo(var2.getEntityType());
   }
}
