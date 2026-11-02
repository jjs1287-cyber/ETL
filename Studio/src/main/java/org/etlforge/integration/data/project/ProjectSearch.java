package org.etlforge.integration.data.project;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ProjectSearch {
   private final IntegrationProject project;
   private final List<Predicate<? super IntegrationEntity>> filters = Lists.newArrayList();

   public static ProjectSearch in(IntegrationProject var0) {
      return new ProjectSearch(var0);
   }

   public ProjectSearch(IntegrationProject var1) {
      this.project = var1;
   }

   public ProjectSearch withFilter(Predicate<? super IntegrationEntity> var1) {
      this.filters.add(var1);
      return this;
   }

   public ProjectSearchResult<IntegrationEntity> search() {
      List var1 = this.project.getAllEntities();
      return ProjectSearchResult.of(Iterables.filter(var1, Predicates.and(this.filters)));
   }

   public <T extends IntegrationEntity> ProjectSearchResult<T> searchFor(Class<T> var1) {
      FluentIterable var2 = this.project.<T>getAllEntitiesOfClass(var1).filter(Predicates.and(this.filters));
      return ProjectSearchResult.of(var2);
   }
}
