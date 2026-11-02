package org.etlforge.integration.activity.ui;

import org.etlforge.integration.activity.ActivitySubjectType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ActivityUtils {
   public static boolean isValidType(IntegrationEntity var0) {
      if (!(var0 instanceof Folder)) {
         return isValidType(var0.getEntityType());
      }

      EntityType var1 = ((Folder)var0).getItemType();
      return !var1.isWizardType() && isValidType(var1);
   }

   public static boolean isValidType(EntityType var0) {
      return var0 == EntityType.Project || var0 == EntityType.Operation || var0.isWizardType();
   }

   public static EntityType toEntityType(ActivitySubjectType var0) {
      switch (var0) {
         case SERVER:
            return null;
         case OPERATION:
            return EntityType.Operation;
         case FOLDER:
            return EntityType.Folder;
         case PROJECT:
            return EntityType.Project;
         default:
            throw new RuntimeException("Unexpected subject type: " + var0);
      }
   }

   private ActivityUtils() {
   }
}
