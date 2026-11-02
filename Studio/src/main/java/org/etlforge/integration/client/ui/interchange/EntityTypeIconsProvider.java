package org.etlforge.integration.client.ui.interchange;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public final class EntityTypeIconsProvider implements TopLevelEntityCategory.IconProvider {
   private final EntityIcons icons;

   public EntityTypeIconsProvider(String var1) {
      this(EntityType.valueOf(var1));
   }

   public EntityTypeIconsProvider(EntityType var1) {
      this.icons = EntityIcons.forType(var1);
   }

   @Override
   public Icon getIcon(EntityIcons.Size var1, EntityIcons.Category var2) {
      return this.icons.getIcon(var2, var1);
   }
}
