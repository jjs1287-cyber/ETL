package org.etlforge.integration.data.entity.factory;

import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EntityFactoryUtils {
   public static String getNewEntityName(IntegrationEntity var0, IntegrationEntity var1) {
      String var2 = "New " + var0.getEntityType().getDisplayName();
      return EntityUtils.getAvailableName(var0, var1, var2);
   }

   private EntityFactoryUtils() {
   }
}
