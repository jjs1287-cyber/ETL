package org.etlforge.integration.client.ui.entity.search;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;

public final class EntitySearchUiBuilder {
   private final ImmutableSet<EntityType> supportedEntityTypes;
   private final SearchType[] searchTypes;

   public EntitySearchUiBuilder(Iterable<EntityType> var1, SearchType[] var2) {
      this.supportedEntityTypes = ImmutableSet.copyOf(var1);
      this.searchTypes = var2;
   }

   public EntitySearchUi buildSearchUi(IntegrationProject var1) {
      EntitySearchModel var2 = this.createModel(var1);
      return new EntitySearchUi(var2);
   }

   private EntitySearchModel createModel(IntegrationProject var1) {
      this.prepareSearchTypes(var1);
      return new EntitySearchModel(var1, this.supportedEntityTypes, this.searchTypes);
   }

   private void prepareSearchTypes(IntegrationProject var1) {
      for (SearchType var5 : this.searchTypes) {
         var5.setProject(var1);
      }
   }
}
