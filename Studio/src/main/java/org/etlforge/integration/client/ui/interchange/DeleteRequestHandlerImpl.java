package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.integration.client.ui.interchange.delete.AffectedDependenciesConfirmer;
import org.etlforge.integration.client.ui.interchange.delete.Dependencies;
import org.etlforge.integration.client.ui.interchange.delete.WizardItemDeleteChecker;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.ui.wait.WaitService;

public final class DeleteRequestHandlerImpl implements DeleteRequestHandler {
   private final ProjectPersistor persistor;
   private WaitService waitService = WaitService.NONE;
   private Predicate<List<? super IntegrationEntity>> deleteGate = Predicates.alwaysTrue();
   private final EntityFilter publicTypesFilter;

   public DeleteRequestHandlerImpl(ProjectPersistor var1, EntityFilter var2) {
      this.persistor = var1;
      this.publicTypesFilter = var2;
   }

   public void setDeleteGate(Predicate<List<? super IntegrationEntity>> var1) {
      this.deleteGate = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public boolean deleteRequested(List<IntegrationEntity> var1) {
      if (!this.isAllowed(var1)) {
         return false;
      } else {
         IntegrationProject var2 = ProjectUtils.getProject(var1);
         DeleteRequestHandlerImpl.DependencyChecker var3 = new DeleteRequestHandlerImpl.DependencyChecker(var2, this.publicTypesFilter, this.persistor, var1);
         if (var3.isGreenLight()) {
            this.delete(var2, var1, var3);
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean isAllowed(List<IntegrationEntity> var1) {
      return this.deleteGate.apply(var1);
   }

   private void delete(final IntegrationProject var1, final List<IntegrationEntity> var2, final DeleteRequestHandlerImpl.DependencyChecker var3) {
      (new BusyWorker(this.waitService) {
         @Override
         protected void doWork() {
            DeleteRequestHandlerImpl.this.deleteImpl(var1, var2, var3);
         }
      }).runInCallingThread();
   }

   private void deleteImpl(IntegrationProject var1, List<IntegrationEntity> var2, DeleteRequestHandlerImpl.DependencyChecker var3) {
      HashSet var4 = Sets.newHashSet(var2);
      var4.addAll(var3.getOrphansToDelete());
      var4.addAll(var3.getDependenciesToDelete());
      IntegrationProjectLock.acquire();

      try {
         var1.deleteItems(var4);
      } finally {
         IntegrationProjectLock.release();
      }

      var3.saveAffectedEntities();
   }

   private static class DependencyChecker {
      private final IntegrationProject project;
      private final ProjectPersistor persistor;
      private final EntityFilter publicTypesFilter;
      private final Set<IntegrationEntity> toBeDeleted;
      private Set<IntegrationEntity> allItemsThatWillBeDeleted;
      private Set<IntegrationEntity> entitiesToSave;
      private Set<IntegrationEntity> orphansToDelete;
      private final Dependencies dependencies;

      public DependencyChecker(IntegrationProject var1, EntityFilter var2, ProjectPersistor var3, List<IntegrationEntity> var4) {
         this.project = var1;
         this.persistor = var3;
         this.publicTypesFilter = var2;
         this.toBeDeleted = Sets.newHashSet();

         for (IntegrationEntity var6 : var4) {
            this.collectEntitiesToBeDeleted(var6);
         }

         this.dependencies = this.getDependencies(this.toBeDeleted);
      }

      private void collectEntitiesToBeDeleted(IntegrationEntity var1) {
         this.toBeDeleted.add(var1);
         if (var1 instanceof Folder) {
            for (IntegrationEntity var3 : var1.getAllChildren()) {
               this.collectEntitiesToBeDeleted(var3);
            }
         }
      }

      private Dependencies getDependencies(Collection<IntegrationEntity> var1) {
         if (this.project == null) {
            return Dependencies.empty();
         }

         Dependencies var2 = new Dependencies(this.project.getCurrentDependencies());
         var2.addDependencies(var1);
         return var2;
      }

      public boolean isGreenLight() {
         boolean var1;
         try {
            var1 = true;
            if (!this.dependencies.isEmpty()) {
               var1 = this.confirmSave(this.dependencies);
            }

            if (var1) {
               this.collectEntitiesToSave(this.dependencies);
               this.collectAllItemsThatWillBeDeleted(this.dependencies);
               WizardItemDeleteChecker var2 = new WizardItemDeleteChecker(this.allItemsThatWillBeDeleted, this.dependencies);
               var1 = var2.isGreenlighted(this.publicTypesFilter);
               if (var1) {
                  this.orphansToDelete = var2.getOrphansToDelete();
               }
            }
         } finally {
            this.dependencies.dispose();
         }

         return var1;
      }

      private boolean confirmSave(Dependencies var1) {
         AffectedDependenciesConfirmer var2 = new AffectedDependenciesConfirmer(var1);
         return var2.isGreenLighted(this.publicTypesFilter);
      }

      private void collectEntitiesToSave(Dependencies var1) {
         for (IntegrationEntity var3 : var1.getDependantObjects()) {
            if (!var1.willBeDeleted(var3)) {
               if (this.entitiesToSave == null) {
                  this.entitiesToSave = Sets.newHashSet();
               }

               this.entitiesToSave.add(var3);
            }
         }
      }

      private void collectAllItemsThatWillBeDeleted(Dependencies var1) {
         this.allItemsThatWillBeDeleted = Sets.newHashSet(this.toBeDeleted);

         for (IntegrationEntity var3 : var1.getDependantObjects()) {
            if (var1.willBeDeleted(var3)) {
               this.allItemsThatWillBeDeleted.add(var3);
            }
         }
      }

      public void saveAffectedEntities() {
         if (this.entitiesToSave != null) {
            DeleteRequestHandlerImpl.SaveJob var1 = new DeleteRequestHandlerImpl.SaveJob(this.persistor, this.entitiesToSave);
            var1.run();
         }
      }

      public Set<IntegrationEntity> getDependenciesToDelete() {
         return this.dependencies.getDependenciesThatWillBeDeleted();
      }

      public Set<IntegrationEntity> getOrphansToDelete() {
         return this.orphansToDelete;
      }
   }

   private static class SaveJob {
      private final ProjectPersistor persistor;
      private final Set<IntegrationEntity> entitiesToSave;
      private final Map<IntegrationEntity, InterchangeSavingException> failures;

      public SaveJob(ProjectPersistor var1, Set<IntegrationEntity> var2) {
         assert var2 != null;
         this.persistor = var1;
         this.entitiesToSave = var2;
         this.failures = Maps.newHashMap();
      }

      public void run() {
         for (IntegrationEntity var2 : this.entitiesToSave) {
            this.saveEntity(var2);
         }

         if (!this.failures.isEmpty()) {
         }
      }

      private void saveEntity(IntegrationEntity var1) {
         try {
            this.persistor.save(var1);
         } catch (InterchangeSavingException var3) {
            this.failures.put(var1, var3);
         }
      }
   }
}
