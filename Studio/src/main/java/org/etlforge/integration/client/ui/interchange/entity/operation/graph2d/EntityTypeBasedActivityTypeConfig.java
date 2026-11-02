package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.base.Preconditions;
import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public abstract class EntityTypeBasedActivityTypeConfig extends ActivityTypeConfig {
   private final EntityType entityType;

   public EntityTypeBasedActivityTypeConfig(PipelineActivityType var1, EntityType var2) {
      super(var1);
      this.entityType = Preconditions.checkNotNull(var2, "entityType");
   }

   @Override
   public final Icon getSmallIcon() {
      return EntityIcons.forType(this.entityType).getDefaultIcon(EntityIcons.Size.SMALL);
   }
}
