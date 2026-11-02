package com.etlforge.integration.client.project.share;

import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class DefaultConflictResolver implements ConflictResolver {
   private final ManagedProject sourceProject;

   public DefaultConflictResolver(ManagedProject var1) {
      this.sourceProject = var1;
   }

   @Override
   public ConflictResolution resolve(NameConflict var1) {
      IntegrationEntity var2 = var1.getCopiedEntity();
      IntegrationEntity var3 = var1.getExistingEntity().getParent();
      String var4 = this.getUniqueName(var2, var3);
      var2.setName(var4);
      return ConflictResolution.CONTINUE;
   }

   @Override
   public ConflictResolution resolve(IdConflict var1) {
      return ConflictResolution.SKIP;
   }

   private String getUniqueName(IntegrationEntity var1, IntegrationEntity var2) {
      int var3 = 1;

      String var4;
      do {
         var4 = var1.getName() + " [" + this.sourceProject.getProject().getName() + "]";
         if (var3 > 1) {
            var4 = var4 + " (" + var3 + ")";
         }

         var3++;
      } while (!this.isNameAvailable(var2, var4));

      return var4;
   }

   private boolean isNameAvailable(IntegrationEntity var1, String var2) {
      for (IntegrationEntity var4 : var1.getAllChildren()) {
         if (var4.getName().equals(var2)) {
            return false;
         }
      }

      return true;
   }
}
