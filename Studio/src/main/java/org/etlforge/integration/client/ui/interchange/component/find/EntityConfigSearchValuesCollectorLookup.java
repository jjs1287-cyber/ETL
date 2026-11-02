package org.etlforge.integration.client.ui.interchange.component.find;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollectorLookup;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EntityConfigSearchValuesCollectorLookup implements SearchValuesCollectorLookup {
   private final EntityConfigLookup entityConfigs;

   public EntityConfigSearchValuesCollectorLookup(EntityConfigLookup var1) {
      this.entityConfigs = Preconditions.checkNotNull(var1, "entityConfigs");
   }

   @Override
   public SearchValuesCollector getSearchValuesCollector(IntegrationEntity var1) {
      EntityConfig var2 = this.entityConfigs.getEntityConfig(var1.getEntityType());
      return var2.getSearchValuesCollector();
   }
}
