package org.etlforge.integration.client.ui.entity.typeselector;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityType;

public interface EntityTypeSelector {
   ImmutableSet<EntityType> getSelection();

   void setSelection(Set<EntityType> var1);

   void addEntityTypeSelectionListener(EntityTypeSelectionListener var1);

   void removeEntityTypeSelectionListener(EntityTypeSelectionListener var1);
}
