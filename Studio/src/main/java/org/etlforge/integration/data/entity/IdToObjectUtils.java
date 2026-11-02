package org.etlforge.integration.data.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class IdToObjectUtils {
   public static <T extends IntegrationEntity> List<T> getEntitiesFromIds(
      Collection<? extends IntegrationEntityId> var0, IntegrationEntityLookup var1, Class<T> var2
   ) {
      ArrayList var3 = new ArrayList();

      for (IntegrationEntityId var5 : var0) {
         var3.add(var1.getEntity(var5, var2));
      }

      return var3;
   }

   public static <T extends IntegrationEntity> List<T> getEntitiesFromIds(IntegrationEntityId[] var0, IntegrationEntityLookup var1, Class<T> var2) {
      ArrayList var3 = new ArrayList();

      for (IntegrationEntityId var7 : var0) {
         var3.add(var1.getEntity(var7, var2));
      }

      return var3;
   }

   private IdToObjectUtils() {
   }
}
