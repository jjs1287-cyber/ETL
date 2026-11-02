package org.etlforge.integration.client.ui.entity.dependencies.graph;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.model.DefaultGraphModel;
import org.etlforge.ui.graph.model.GraphModel;
import org.etlforge.ui.graph.model.GraphModelListener;

final class DependencyGraphModel implements GraphModel {
   private final IntegrationEntity entity;
   private final DefaultGraphModel impl;

   public DependencyGraphModel(IntegrationEntity var1, ProjectDependencies var2) {
      this.entity = var1;
      this.impl = new DefaultGraphModel();
      Predicate var3 = Predicates.not(EntityFilters.BUILT_IN);

      for (Object __etlItem27 : var2.getDependingObjects(var1).filter(var3)) {
         IntegrationEntity var5 = (IntegrationEntity) __etlItem27;
         this.impl.addEdge(var5, var1);
      }

      for (Object __etlItem31 : var2.getRequiredObjects(var1).filter(var3)) {
         IntegrationEntity var7 = (IntegrationEntity) __etlItem31;
         this.impl.addEdge(var1, var7);
      }
   }

   public IntegrationEntity getEntity() {
      return this.entity;
   }

   public Iterable<IntegrationEntity> getDependsOn() {
      return Iterables.transform(this.impl.getIncomingEdges(this.entity), new Function<GraphEdge, IntegrationEntity>() {
         public IntegrationEntity apply(GraphEdge var1) {
            return (IntegrationEntity)var1.from();
         }
      });
   }

   public Iterable<IntegrationEntity> getRequiredBy() {
      return Iterables.transform(this.impl.getOutgoingEdges(this.entity), new Function<GraphEdge, IntegrationEntity>() {
         public IntegrationEntity apply(GraphEdge var1) {
            return (IntegrationEntity)var1.to();
         }
      });
   }

   @Override
   public boolean contains(Object var1) {
      return this.impl.contains(var1);
   }

   @Override
   public Collection<? extends GraphEdge> getEdges() {
      return this.impl.getEdges();
   }

   @Override
   public Collection<? extends Object> getNodes() {
      HashSet var1 = Sets.newHashSet(this.impl.getNodes());
      var1.add(this.entity);
      return var1;
   }

   @Override
   public Collection<? extends GraphEdge> getIncomingEdges(Object var1) {
      return this.impl.getIncomingEdges(var1);
   }

   @Override
   public Collection<? extends GraphEdge> getOutgoingEdges(Object var1) {
      return this.impl.getOutgoingEdges(var1);
   }

   @Override
   public boolean isGroup(Object var1) {
      return false;
   }

   @Override
   public Object getParentOf(Object var1) {
      return null;
   }

   @Override
   public void addModelListener(GraphModelListener var1) {
   }

   @Override
   public void removeModelListener(GraphModelListener var1) {
   }
}
