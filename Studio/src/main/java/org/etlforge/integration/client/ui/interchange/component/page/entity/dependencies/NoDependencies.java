package org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies;

import java.beans.PropertyChangeSupport;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class NoDependencies implements EntityDependenciesModel {
   @Override
   public boolean isEmpty() {
      return true;
   }

   @Override
   public void dispose() {
   }

   @Override
   public Property<List<IntegrationEntity>> getDependants() {
      return new ListProperty<>("dependants", new PropertyChangeSupport(this));
   }
}
