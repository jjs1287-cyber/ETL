package org.etlforge.integration.client.project.validation;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectValidationContext;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectDependencies;
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
import org.etlforge.system.callback.Receiver;

@Deprecated
public final class DefaultValidationModel implements ValidationModel {
   private final ManagedProject project;
   private final ValidationContext context;
   private final List<ValidationModelListener> listeners = new CopyOnWriteArrayList<>();
   private Map<IntegrationEntity, ValidationResult> results = Maps.newHashMap();
   private Map<IntegrationEntity, ValidationStatus> compoundStatuses = Maps.newHashMap();
   private final Object lock = new Object();

   public DefaultValidationModel(ManagedProject var1) {
      this.project = var1;
      this.context = new ProjectValidationContext(var1);
      var1.getProject().addProjectListener(new DefaultValidationModel.ProjectListenerImpl());
   }

   @Override
   public IntegrationProject getProject() {
      return this.project.getProject();
   }

   @Override
   public ValidationStatus validateProject() {
      final HashMap var1 = Maps.newHashMap();
      this.project.getProject().visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1x) {
            var1.put(var1x, DefaultValidationModel.this.validateSingleItem(var1x));
         }
      });
      ValidationStatus var2;
      synchronized (this.lock) {
         this.results = var1;
         this.compoundStatuses = Maps.newHashMap();
         var2 = this.getStatus(this.project.getProject(), true);
      }

      this.fireValidationEvent();
      return var2;
   }

   @Override
   public ValidationStatus validateItem(IntegrationEntity var1) {
      DefaultValidationModel.Validator var2 = new DefaultValidationModel.Validator(var1);
      Map var3 = var2.run();
      ValidationStatus var4;
      synchronized (this.lock) {
         for (Object __etlItem81 : var3.entrySet()) {
            Entry var7 = (Entry) __etlItem81;
            this.results.put((IntegrationEntity)var7.getKey(), (ValidationResult)var7.getValue());
         }

         this.compoundStatuses = Maps.newHashMap();
         var4 = this.getStatus(var1, true);
      }

      this.fireValidationEvent();
      return var4;
   }

   @Override
   public ValidationStatus getStatus(IntegrationEntity var1, boolean var2) {
      synchronized (this.lock) {
         if (var2) {
            ValidationStatus var4 = this.compoundStatuses.get(var1);
            if (var4 == null) {
               var4 = new DefaultValidationModel.CompoundStateBuilder(var1).getStatus();
               this.compoundStatuses.put(var1, var4);
            }

            return var4;
         } else {
            return this.getValidationState(var1).status();
         }
      }
   }

   private ValidationResult getValidationState(IntegrationEntity var1) {
      ValidationResult var2;
      synchronized (this.lock) {
         var2 = this.results.get(var1);
      }

      if (var2 == null) {
         var2 = ValidationResult.VALID;
      }

      return var2;
   }

   @Override
   public EntityCollectionValidationResult getCompoundValidationState(IntegrationEntity var1) {
      synchronized (this.lock) {
         return new DefaultValidationModel.CompoundStateBuilder(var1).build();
      }
   }

   @Override
   public void addListener(ValidationModelListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(ValidationModelListener var1) {
      this.listeners.remove(var1);
   }

   private ValidationResult validateSingleItem(IntegrationEntity var1) {
      return var1.validate(this.context);
   }

   private void fireValidationEvent() {
      ValidationModelEvent var1 = new ValidationModelEvent(this);

      for (ValidationModelListener var3 : this.listeners) {
         var3.validationWasPerformed(var1);
      }
   }

   @Override
   public String toString() {
      return "Validation state for the project \"" + this.getProject().getName() + "\": " + this.getStatus(this.getProject(), true);
   }

   private class CompoundStateBuilder {
      private final IntegrationEntity entity;

      public CompoundStateBuilder(IntegrationEntity var2) {
         this.entity = var2;
      }

      public EntityCollectionValidationResult build() {
         Collection var1 = this.collectEntities();
         EntityCollectionValidationResultBuilder var2 = new EntityCollectionValidationResultBuilder();

         for (Object __etlItem168 : var1) {
            IntegrationEntity var4 = (IntegrationEntity) __etlItem168;
            var2.add(var4, DefaultValidationModel.this.getValidationState(var4));
         }

         return var2.getResult();
      }

      public ValidationStatus getStatus() {
         ValidationStatus var1 = ValidationStatus.VALID;

         for (IntegrationEntity var3 : this.collectEntities()) {
            ValidationStatus var4 = DefaultValidationModel.this.getStatus(var3, false);
            if (var4 == ValidationStatus.INVALID) {
               return var4;
            }

            if (var4 == ValidationStatus.WARNING) {
               var1 = var4;
            }
         }

         return var1;
      }

      private Collection<IntegrationEntity> collectEntities() {
         if (this.entity instanceof IntegrationProject) {
            return ((IntegrationProject)this.entity).getAllEntities();
         } else if (this.entity instanceof Folder) {
            List var2 = ((Folder)this.entity).getAllItemsRecursively(IntegrationEntity.class);
            Iterables.removeIf(var2, Predicates.instanceOf(Folder.class));
            return var2;
         } else {
            HashSet var1 = Sets.newHashSet(DefaultValidationModel.this.project.getDependencyStore().getRequiredObjectsRecursively(this.entity));
            var1.add(this.entity);
            return var1;
         }
      }
   }

   private static class CyclicGuard {
      private final Set<IntegrationEntity> items = Sets.newHashSet();

      private CyclicGuard() {
      }

      public boolean isNew(IntegrationEntity var1) {
         return this.items.add(var1);
      }
   }

   private final class ProjectListenerImpl extends ProjectListener.Adapter {
      private ProjectListenerImpl() {
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         synchronized (DefaultValidationModel.this.lock) {
            for (IntegrationEntity var4 : var1.getEntities()) {
               DefaultValidationModel.this.results.remove(var4);
               DefaultValidationModel.this.compoundStatuses.remove(var4);
            }
         }

         DefaultValidationModel.this.validateProject();
      }
   }

   private static class ValidationOrder implements Iterable<IntegrationEntity> {
      private final SortedMap<Integer, List<IntegrationEntity>> steps = Maps.newTreeMap();
      private final ProjectDependencies dependencies;

      public ValidationOrder(ManagedProject var1, IntegrationEntity var2) {
         this.steps.put(0, Lists.newArrayList(var2));
         this.dependencies = var1.getDependencyStore();
         this.collectDependingObjects(var2);
         this.collectRequiredObjects(var2);
      }

      @Override
      public Iterator<IntegrationEntity> iterator() {
         HashSet var1 = Sets.newHashSet();

         for (List var3 : this.steps.values()) {
            var1.addAll(var3);
         }

         return var1.iterator();
      }

      private void collectDependingObjects(IntegrationEntity var1) {
         this.collectDependendingObjects(var1, -1, new DefaultValidationModel.CyclicGuard());
      }

      private void collectRequiredObjects(IntegrationEntity var1) {
         this.collectRequiredObjects(var1, 1, new DefaultValidationModel.CyclicGuard());
      }

      private void collectDependendingObjects(IntegrationEntity var1, int var2, DefaultValidationModel.CyclicGuard var3) {
         if (var3.isNew(var1)) {
            for (IntegrationEntity var5 : this.dependencies.getDependingObjects(var1)) {
               this.addStep(var5, var2);
               this.collectDependendingObjects(var5, var2 - 1, var3);
            }
         }
      }

      private void collectRequiredObjects(IntegrationEntity var1, int var2, DefaultValidationModel.CyclicGuard var3) {
         if (var3.isNew(var1)) {
            for (IntegrationEntity var5 : this.dependencies.getRequiredObjects(var1)) {
               this.addStep(var5, var2);
               this.collectDependendingObjects(var5, var2 + 1, var3);
            }
         }
      }

      private void addStep(IntegrationEntity var1, int var2) {
         List var3 = this.steps.get(var2);
         if (var3 == null) {
            var3 = Lists.newArrayList();
            this.steps.put(var2, var3);
         }

         var3.add(var1);
      }
   }

   private final class Validator {
      private final DefaultValidationModel.ValidationOrder order;
      private final Map<IntegrationEntity, ValidationResult> validated;

      public Validator(IntegrationEntity var2) {
         this.order = new DefaultValidationModel.ValidationOrder(DefaultValidationModel.this.project, var2);
         this.validated = Maps.newHashMap();
      }

      public Map<IntegrationEntity, ValidationResult> run() {
         for (IntegrationEntity var2 : this.order) {
            this.validateEntity(var2);
         }

         return this.validated;
      }

      private void validateEntity(IntegrationEntity var1) {
         if (!this.validated.containsKey(var1)) {
            ValidationResult var2 = DefaultValidationModel.this.validateSingleItem(var1);
            this.validated.put(var1, var2);
         }
      }
   }
}
