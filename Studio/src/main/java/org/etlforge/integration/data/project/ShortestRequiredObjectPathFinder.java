package org.etlforge.integration.data.project;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.graph.ShortestPathFinder;

public final class ShortestRequiredObjectPathFinder {
   private final ShortestPathFinder<IntegrationEntity> impl;

   public ShortestRequiredObjectPathFinder(final ProjectDependencies var1) {
      this.impl = ShortestPathFinder.in(new ShortestPathFinder.Graph<IntegrationEntity>() {
         public Iterable<IntegrationEntity> neighbors(IntegrationEntity var1x) {
            return var1.getRequiredObjects(var1x);
         }
      });
   }

   public ImmutableList<IntegrationEntity> shortestPath(IntegrationEntity var1, IntegrationEntity var2) {
      return this.impl.findPath(var1, var2);
   }
}
