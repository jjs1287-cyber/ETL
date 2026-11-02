package org.etlforge.integration.client.project.event;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.EventObject;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EntitySavedEvent extends EventObject {
   private final IntegrationEntity entity;

   public EntitySavedEvent(ManagedProject var1, IntegrationEntity var2) {
      super(var1);
      this.entity = Preconditions.checkNotNull(var2, "entity");
   }

   public ManagedProject getSource() {
      return (ManagedProject)super.getSource();
   }

   public IntegrationEntity getEntity() {
      return this.entity;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("entity", this.entity).toString();
   }
}
