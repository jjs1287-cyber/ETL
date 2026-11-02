package org.etlforge.integration.client.ui.interchange;

import java.util.Collection;
import java.util.Collections;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProjectLock;

final class SaveToDiskPersistor implements ProjectPersistor {
   private final CurrentProjectService currentProjectService;

   public SaveToDiskPersistor(CurrentProjectService var1) {
      this.currentProjectService = var1;
   }

   @Override
   public void save(IntegrationEntity var1) throws InterchangeSavingException {
      SaveToDiskPersistor.EntitySaver var2 = new SaveToDiskPersistor.EntitySaver(Collections.singletonList(var1));
      this.runSaveJob(var2);
   }

   @Override
   public void saveProjectRelations() throws InterchangeSavingException {
      SaveToDiskPersistor.StructureSaver var1 = new SaveToDiskPersistor.StructureSaver();
      this.runSaveJob(var1);
   }

   private void runSaveJob(SaveToDiskPersistor.SaveJob var1) throws InterchangeSavingException {
      IntegrationProjectLock.acquire();

      try {
         var1.run();
      } catch (InterchangeSavingException var6) {
         ErrorLog.attention(SaveToDiskPersistor.class, PackageResources.SaveWorker.SAVE_FAILED, var6);
         throw var6;
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private class EntitySaver implements SaveToDiskPersistor.SaveJob {
      private final Collection<? extends IntegrationEntity> entities;

      public EntitySaver(Collection<? extends IntegrationEntity> var2) {
         this.entities = var2;
      }

      @Override
      public void run() throws InterchangeSavingException {
         ManagedProject var1 = SaveToDiskPersistor.this.currentProjectService.getManagedProject();

         for (IntegrationEntity var3 : this.entities) {
            var1.saveIntegrationEntity(var3);
         }

         var1.saveProjectRelationships();
      }
   }

   private interface SaveJob {
      void run() throws InterchangeSavingException;
   }

   private class StructureSaver implements SaveToDiskPersistor.SaveJob {
      private StructureSaver() {
      }

      @Override
      public void run() throws InterchangeSavingException {
         ManagedProject var1 = SaveToDiskPersistor.this.currentProjectService.getManagedProject();
         var1.saveProjectRelationships();
      }
   }
}
