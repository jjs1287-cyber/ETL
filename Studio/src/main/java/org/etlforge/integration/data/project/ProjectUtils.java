package org.etlforge.integration.data.project;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;

public final class ProjectUtils {
   public static IntegrationProject getProject(Iterable<IntegrationEntity> var0) {
      for (IntegrationEntity var2 : var0) {
         IntegrationProject var3 = getProject(var2);
         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }

   public static IntegrationProject getProject(IntegrationEntity var0) {
      while (var0 != null && !(var0 instanceof IntegrationProject)) {
         var0 = var0.getParent();
      }

      return (IntegrationProject)var0;
   }

   public static IntegrationEntityLookup getLookup(IntegrationEntity var0) {
      IntegrationProject var1 = getProject(var0);
      return var1 != null ? var1.getItemLookup() : new IntegrationEntityMapLookup();
   }

   private ProjectUtils() {
   }
}
