package org.etlforge.integration.client.project.validation;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectValidationContext;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.project.validation.ValidationModelEvent;
import org.etlforge.integration.data.project.validation.ValidationModelListener;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.EntityCollectionValidationResultBuilder;
import org.etlforge.integration.data.validation.ValidationContext;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.system.Disposable;

public final class DefaultValidationModel2 implements ValidationModel, Disposable {
   private final ConcurrentMap<IntegrationEntity, ValidationResult> individualResults = Maps.newConcurrentMap();
   private final IntegrationProject project;
   private final Supplier<DependencyStore> dependenciesSupplier;
   private final ValidationContext context;
   private final DefaultValidationModel2.SecondHandValidationEffects secondHandEffects = new DefaultValidationModel2.SecondHandValidationEffects();
   private final DefaultValidationModel2.ProjectEventHandler projectEventHandler = new DefaultValidationModel2.ProjectEventHandler();
   private final List<ValidationModelListener> listeners = Lists.newCopyOnWriteArrayList();

   public DefaultValidationModel2(final ManagedProject var1) {
      this.context = new ProjectValidationContext(var1);
      this.project = var1.getProject();
      this.dependenciesSupplier = new Supplier<DependencyStore>() {
         public DependencyStore get() {
            return var1.getDependencyStore();
         }
      };
      this.project.addProjectListener(this.projectEventHandler);
   }

   @VisibleForTesting
   DefaultValidationModel2(IntegrationProject var1, Supplier<DependencyStore> var2, ValidationContext var3) {
      this.project = var1;
      this.dependenciesSupplier = var2;
      this.context = var3;
      var1.addProjectListener(this.projectEventHandler);
   }

   @Override
   public void dispose() {
      this.project.removeProjectListener(this.projectEventHandler);
   }

   @Override
   public IntegrationProject getProject() {
      return this.project;
   }

   @Override
   public ValidationStatus validateProject() {
      this.clearAllData();

      for (IntegrationEntity var3 : Iterables.filter(this.project.getAllEntities(), Predicates.not(Predicates.instanceOf(Folder.class)))) {
         ValidationStatus var4 = this.validateSingleItem(var3);
         if (var4 != ValidationStatus.VALID) {
            this.secondHandEffects.updateForInvalidEntity(var3);
         }
      }

      ValidationStatus var5 = mostSevereStatus(this.individualResults.values());
      this.fireValidationEvent();
      return var5;
   }

   private void clearAllData() {
      this.individualResults.clear();
      this.secondHandEffects.clear();
   }

   private ValidationStatus validateSingleItem(IntegrationEntity var1) {
      this.individualResults.remove(var1);
      this.secondHandEffects.forgetEffectOfEntity(var1);
      ValidationResult var2 = this.individualResults.get(var1);
      if (var2 == null) {
         var2 = var1.validate(this.context);
         this.individualResults.put(var1, var2);
      }

      return var2.status();
   }

   private void fireValidationEvent() {
      ValidationModelEvent var1 = new ValidationModelEvent(this);

      for (ValidationModelListener var3 : this.listeners) {
         var3.validationWasPerformed(var1);
      }
   }

   @Override
   public ValidationStatus validateItem(IntegrationEntity var1) {
      if (!var1.isTransient() && !(var1 instanceof Folder)) {
         ValidationStatus var2 = this.validateSingleItem(var1);
         this.secondHandEffects.updateForValidatedEntity(var1, var2);
         ValidationStatus var3 = this.getStatus(var1, true);
         this.fireValidationEvent();
         return var3;
      } else {
         return ValidationStatus.VALID;
      }
   }

   @Override
   public EntityCollectionValidationResult getCompoundValidationState(IntegrationEntity var1) {
      DefaultValidationModel2.CompoundStatusBuilder var2 = new DefaultValidationModel2.CompoundStatusBuilder(var1);
      return var2.build();
   }

   @Override
   public ValidationStatus getStatus(IntegrationEntity var1, boolean var2) {
      if (var1 instanceof IntegrationProject) {
         return mostSevereStatus(this.individualResults.values());
      }

      ValidationStatus var3;
      if (var1 instanceof Folder) {
         var3 = ValidationStatus.VALID;
      } else {
         var3 = this.getIndividualStatusIfKnown(var1).or(ValidationStatus.VALID);
         if (!var2 || var3 == ValidationStatus.INVALID) {
            return var3;
         }
      }

      assert var2;
      assert var3 != ValidationStatus.INVALID;
      ValidationStatus var4 = this.secondHandEffects.getSecondHandStatus(var1);
      return Ordering.natural().max(var3, var4);
   }

   private static ValidationStatus mostSevereStatus(Iterable<ValidationResult> var0) {
      return ValidationStatus.mostSevere(Iterables.transform(var0, ValidationResult.TO_STATUS));
   }

   private Optional<ValidationStatus> getIndividualStatusIfKnown(IntegrationEntity var1) {
      ValidationResult var2 = this.individualResults.get(var1);
      return var2 != null ? Optional.of(var2.status()) : Optional.absent();
   }

   @Override
   public void addListener(ValidationModelListener var1) {
      this.listeners.add(Preconditions.checkNotNull(var1, "lst"));
   }

   @Override
   public void removeListener(ValidationModelListener var1) {
      this.listeners.remove(var1);
   }

   private class CompoundStatusBuilder {
      private final Iterable<IntegrationEntity> toValidate;
      private final EntityCollectionValidationResultBuilder builder;

      public CompoundStatusBuilder(IntegrationEntity var2) {
         this.toValidate = this.collectEntitiesToValidate(var2);
         this.builder = new EntityCollectionValidationResultBuilder(var2, DefaultValidationModel2.this.dependenciesSupplier.get());
      }

      private Iterable<IntegrationEntity> collectEntitiesToValidate(IntegrationEntity var1) {
         if (var1 instanceof IntegrationProject) {
            return ((IntegrationProject)var1).getAllEntities();
         } else if (var1 instanceof Folder) {
            List var4 = ((Folder)var1).getAllItemsRecursively(IntegrationEntity.class);
            Iterables.removeIf(var4, Predicates.instanceOf(Folder.class));
            return var4;
         } else {
            DependencyStore var2 = DefaultValidationModel2.this.dependenciesSupplier.get();
            HashSet var3 = Sets.newHashSet(var2.getRequiredObjectsRecursively(var1));
            var3.add(var1);
            return var3;
         }
      }

      public EntityCollectionValidationResult build() {
         for (IntegrationEntity var2 : this.toValidate) {
            ValidationResult var3 = this.validationResultFor(var2);
            this.builder.add(var2, var3);
         }

         return this.builder.getResult();
      }

      private ValidationResult validationResultFor(IntegrationEntity var1) {
         ValidationResult var2 = DefaultValidationModel2.this.individualResults.get(var1);
         if (var2 == null) {
            var2 = ValidationResult.VALID;
         }

         return var2;
      }
   }

   private class ProjectEventHandler extends ProjectListener.Adapter {
      private ProjectEventHandler() {
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         DefaultValidationModel2.this.validateProject();
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            DefaultValidationModel2.this.secondHandEffects.handleMovedEntity(var3);
         }
      }
   }

   private class SecondHandValidationEffects {
      private final Multimap<IntegrationEntity, IntegrationEntity> invalidItemCascade = HashMultimap.create();
      private final Multimap<IntegrationEntity, IntegrationEntity> inheritedInvalidStatii = HashMultimap.create();
      private final ConcurrentMap<IntegrationEntity, ValidationStatus> inheritedStatus = Maps.newConcurrentMap();
      private final Object lock = new Object();

      private SecondHandValidationEffects() {
      }

      public void clear() {
         synchronized (this.lock) {
            this.invalidItemCascade.clear();
            this.inheritedInvalidStatii.clear();
            this.inheritedStatus.clear();
         }
      }

      public void forgetEffectOfEntity(IntegrationEntity var1) {
         for (IntegrationEntity var4 : this.removeInvalidItemCascade(var1)) {
            synchronized (this.lock) {
               this.inheritedInvalidStatii.remove(var4, var1);
            }

            this.calculateInheritedStatus(var4);
         }
      }

      private Collection<IntegrationEntity> removeInvalidItemCascade(IntegrationEntity var1) {
         synchronized (this.lock) {
            return this.invalidItemCascade.removeAll(var1);
         }
      }

      private ValidationStatus calculateInheritedStatus(IntegrationEntity var1) {
         Collection var2;
         synchronized (this.lock) {
            var2 = this.inheritedInvalidStatii.get(var1);
         }

         if (var2.isEmpty()) {
            this.inheritedStatus.remove(var1);
            return ValidationStatus.VALID;
         } else {
            Iterable var6 = Iterables.transform(var2, new Function<IntegrationEntity, ValidationStatus>() {
               public ValidationStatus apply(IntegrationEntity var1) {
                  return DefaultValidationModel2.this.getIndividualStatusIfKnown(var1).or(ValidationStatus.VALID);
               }
            });
            ValidationStatus var4 = ValidationStatus.mostSevere(var6);
            this.inheritedStatus.put(var1, var4);
            return var4;
         }
      }

      public void updateForValidatedEntity(IntegrationEntity var1, ValidationStatus var2) {
         if (var2 != ValidationStatus.VALID) {
            this.updateForInvalidEntity(var1);
         }

         DependencyStore var3 = DefaultValidationModel2.this.dependenciesSupplier.get();
         HashSet var4 = Sets.newHashSet(var3.getRequiredObjectsRecursively(var1));
         var4.remove(var1);

         for (Object __etlItem302 : var4) {
            IntegrationEntity var6 = (IntegrationEntity) __etlItem302;
            ValidationStatus var7 = DefaultValidationModel2.this.getIndividualStatusIfKnown(var6).or(ValidationStatus.VALID);
            if (var7 != ValidationStatus.VALID) {
               synchronized (this.lock) {
                  this.invalidItemCascade.put(var6, var1);
                  this.inheritedInvalidStatii.put(var1, var6);
               }
            }
         }

         this.calculateInheritedStatus(var1);
      }

      public void updateForInvalidEntity(IntegrationEntity var1) {
         DependencyStore var2 = DefaultValidationModel2.this.dependenciesSupplier.get();
         HashSet var3 = Sets.newHashSet(var2.getDependingObjectsRecursively(var1));
         Set var4 = this.collectAllParents(var3);
         var3.addAll(var4);
         this.addAllParents(var1, var3);
         synchronized (this.lock) {
            this.invalidItemCascade.putAll(var1, var3);
         }

         for (Object __etlItem325 : var3) {
            IntegrationEntity var6 = (IntegrationEntity) __etlItem325;
            synchronized (this.lock) {
               this.inheritedInvalidStatii.put(var6, var1);
            }

            this.calculateInheritedStatus(var6);
         }
      }

      private Set<IntegrationEntity> collectAllParents(Iterable<IntegrationEntity> var1) {
         HashSet var2 = Sets.newHashSet();

         for (IntegrationEntity var4 : var1) {
            this.addAllParents(var4, var2);
         }

         return var2;
      }

      private void addAllParents(IntegrationEntity var1, Set<IntegrationEntity> var2) {
         for (IntegrationEntity var3 = var1.getParent(); var3 instanceof Folder; var3 = var3.getParent()) {
            var2.add(var3);
         }
      }

      public ValidationStatus getSecondHandStatus(IntegrationEntity var1) {
         ValidationStatus var2 = this.inheritedStatus.get(var1);
         if (var2 == null) {
            var2 = this.calculateInheritedStatus(var1);
         }

         return var2;
      }

      public void handleMovedEntity(IntegrationEntity var1) {
         Collection var2 = this.invalidItemCascade.get(var1);
         if (!var2.isEmpty()) {
            for (Object __etlItem362 : FluentIterable.from(var2).filter(Folder.class).toImmutableSet()) {
               Folder var5 = (Folder) __etlItem362;
               synchronized (this.lock) {
                  this.invalidItemCascade.remove(var1, var5);
                  this.inheritedInvalidStatii.remove(var5, var1);
               }

               this.calculateInheritedStatus(var5);
            }

            for (IntegrationEntity var11 = var1.getParent(); var11 instanceof Folder; var11 = var11.getParent()) {
               synchronized (this.lock) {
                  this.invalidItemCascade.put(var1, var11);
                  this.inheritedInvalidStatii.put(var11, var1);
               }

               this.calculateInheritedStatus(var11);
            }
         }
      }
   }
}
