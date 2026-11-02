package org.etlforge.integration.uidelegate.entity;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class IconStore {
   public static Icon defaultIcon(IntegrationEntity var0) {
      EntityIcons var1 = getIcons(var0.getEntityType());
      return var1.getDefaultIcon(EntityIcons.Size.SMALL);
   }

   private static EntityIcons getIcons(EntityType var0) {
      return EntityIcons.forType(var0);
   }

   public static EntityIcons getIcons(IntegrationEntity var0) {
      return getIcons(var0.getEntityType());
   }

   public static Icon validationDecoratedIcon(IntegrationEntity var0) {
      EntityIcons var1 = getIcons(var0.getEntityType());
      EntityIcons.Category var2 = EntityIcons.Category.fromValidationStatus(var0.getValidationStatus());
      return var1.getIcon(var2, EntityIcons.Size.SMALL);
   }
}
