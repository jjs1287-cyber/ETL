package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaDataLookup;
import org.etlforge.integration.data.entity.EntityType;

public final class EntityConfigSearchAndReplaceMetaDataLookup implements SearchAndReplaceMetaDataLookup {
   private final EntityConfigLookup entityConfigs;

   public EntityConfigSearchAndReplaceMetaDataLookup(EntityConfigLookup var1) {
      this.entityConfigs = var1;
   }

   @Override
   public SearchAndReplaceMetaData getTypeMetaData(EntityType var1) {
      EntityConfig var2 = this.entityConfigs.getEntityConfig(var1);
      return var2.getSearchAndReplaceMetaData();
   }
}
