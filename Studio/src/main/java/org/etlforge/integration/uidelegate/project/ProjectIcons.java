package org.etlforge.integration.uidelegate.project;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public final class ProjectIcons {
   public static final Icon PROJECT_16 = getIcon(EntityIcons.Size.SMALL);
   public static final Icon PROJECT_24 = getIcon(EntityIcons.Size.MEDIUM);
   public static final Icon PROJECT_32 = getIcon(EntityIcons.Size.LARGE);

   private static Icon getIcon(EntityIcons.Size var0) {
      return EntityIcons.forType(EntityType.Project).getDefaultIcon(var0);
   }

   private ProjectIcons() {
   }
}
