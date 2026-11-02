package org.etlforge.integration.client.ui.entity.typeselector;

import com.google.common.collect.ImmutableSet;
import java.util.EventObject;
import org.etlforge.integration.data.entity.EntityType;

public class EntityTypeSelectionEvent extends EventObject {
   private final ImmutableSet<EntityType> oldSelection;
   private final ImmutableSet<EntityType> newSelection;

   public EntityTypeSelectionEvent(EntityTypeSelector var1, ImmutableSet<EntityType> var2, ImmutableSet<EntityType> var3) {
      super(var1);
      this.oldSelection = var2;
      this.newSelection = var3;
   }

   public EntityTypeSelector getSource() {
      return (EntityTypeSelector)super.getSource();
   }

   public ImmutableSet<EntityType> getOldSelection() {
      return this.oldSelection;
   }

   public ImmutableSet<EntityType> getNewSelection() {
      return this.newSelection;
   }
}
