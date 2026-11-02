package org.etlforge.integration.data.validation;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.data.project.ShortestRequiredObjectPathFinder;

public final class EntityCollectionValidationResultBuilder {
   private final Map<IntegrationEntity, ValidationResult> results = Maps.newLinkedHashMap();
   private final Optional<IntegrationEntity> root;
   private final Optional<ShortestRequiredObjectPathFinder> pathFinder;
   private final Map<IntegrationEntity, ImmutableList<IntegrationEntity>> pathsFromRoot = Maps.newHashMap();

   public EntityCollectionValidationResultBuilder() {
      this.root = Optional.absent();
      this.pathFinder = Optional.absent();
   }

   public EntityCollectionValidationResultBuilder(IntegrationEntity var1, ProjectDependencies var2) {
      this.root = Optional.of(var1);
      this.pathFinder = Optional.of(new ShortestRequiredObjectPathFinder(var2));
   }

   public void add(IntegrationEntity var1, ValidationResult var2) {
      Preconditions.checkNotNull(var1, "entity");
      Preconditions.checkNotNull(var2, "result");
      this.results.put(var1, var2);
      ImmutableList var3 = this.getPathFromRoot(var1);
      this.pathsFromRoot.put(var1, var3);
   }

   private ImmutableList<IntegrationEntity> getPathFromRoot(IntegrationEntity var1) {
      if (!this.root.isPresent()) {
         return ImmutableList.of();
      } else if (this.root.get() == var1) {
         return ImmutableList.of(var1);
      } else if (this.root.get() instanceof IntegrationProject) {
         EntityPath var3 = new EntityPath(var1, true);
         return ImmutableList.copyOf(var3);
      } else {
         ImmutableList var2 = this.pathFinder.get().shortestPath(this.root.get(), var1);
         return var2.isEmpty() ? ImmutableList.of(var1) : var2;
      }
   }

   public EntityCollectionValidationResult getResult() {
      return new EntityCollectionValidationResultBuilder.ResultImpl(this.root, this.results, this.pathsFromRoot);
   }

   private static class ResultImpl implements EntityCollectionValidationResult {
      private final Optional<IntegrationEntity> root;
      private final ImmutableMap<IntegrationEntity, ValidationResult> results;
      private final ImmutableMap<IntegrationEntity, ImmutableList<IntegrationEntity>> pathsFromRoot;
      private final ValidationStatus status;

      public ResultImpl(
         Optional<IntegrationEntity> var1, Map<IntegrationEntity, ValidationResult> var2, Map<IntegrationEntity, ImmutableList<IntegrationEntity>> var3
      ) {
         this.root = var1;
         this.results = ImmutableMap.copyOf(var2);
         this.pathsFromRoot = ImmutableMap.copyOf(var3);
         this.status = ValidationStatus.mostSevere(Iterables.transform(var2.values(), ValidationResult.TO_STATUS));
      }

      @Override
      public ImmutableSet<IntegrationEntity> getValidatedEntities() {
         return ImmutableSet.copyOf(this.results.keySet());
      }

      @Override
      public ValidationResult getValidationResult(IntegrationEntity var1) {
         ValidationResult var2 = this.results.get(var1);
         return var2 == null ? ValidationResult.VALID : var2;
      }

      @Override
      public Optional<IntegrationEntity> getRoot() {
         return this.root;
      }

      @Override
      public ImmutableList<IntegrationEntity> getPathFromRoot(IntegrationEntity var1) {
         Preconditions.checkArgument(this.pathsFromRoot.containsKey(var1), "This instance does not know about entity");
         ImmutableList var2 = this.pathsFromRoot.get(var1);
         return var2.isEmpty() ? ImmutableList.of(var1) : var2;
      }

      @Override
      public boolean isEverythingOk() {
         for (ValidationResult var2 : this.results.values()) {
            if (!var2.isEmpty()) {
               return false;
            }
         }

         return true;
      }

      @Override
      public boolean isFatal() {
         return this.status() == ValidationStatus.INVALID;
      }

      @Override
      public ValidationStatus status() {
         return this.status;
      }
   }
}
