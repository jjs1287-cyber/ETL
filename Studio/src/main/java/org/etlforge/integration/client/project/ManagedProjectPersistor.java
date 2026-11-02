package org.etlforge.integration.client.project;

import java.io.File;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;

final class ManagedProjectPersistor implements ExtendedProjectPersistor, FileBasedProjectPersistor {
   private final ManagedProjectImpl project;
   private final ExtendedProjectPersistor impl;
   private boolean readOnly;

   ManagedProjectPersistor(ManagedProjectImpl var1) {
      this.project = var1;
      this.impl = ProjectPersistorFactory.newPersistor(var1);
   }

   public void setReadOnly(boolean var1) {
      this.readOnly = var1;
   }

   @Override
   public File getFile(IntegrationEntity var1) {
      return this.impl instanceof FileBasedProjectPersistor ? ((FileBasedProjectPersistor)this.impl).getFile(var1) : null;
   }

   @Override
   public void saveDeletedItemsInformation() throws InterchangeSavingException {
      this.checkReadOnly();
      this.impl.saveDeletedItemsInformation();
   }

   private void checkReadOnly() {
      if (this.readOnly) {
         throw new UnsupportedOperationException("The project is in a read-only state");
      }
   }

   @Override
   public void save(IntegrationEntity var1) throws InterchangeSavingException {
      this.checkReadOnly();
      this.impl.save(var1);
      this.project.entityWasSaved(var1);
   }

   @Override
   public void savePluginInfo() throws InterchangeSavingException {
      this.checkReadOnly();
      this.impl.savePluginInfo();
   }

   @Override
   public void saveProjectRelations() throws InterchangeSavingException {
      this.checkReadOnly();
      this.impl.saveProjectRelations();
   }

   @Override
   public void saveUiState(ProjectUiState var1) throws InterchangeSavingException {
      this.checkReadOnly();
      this.impl.saveUiState(var1);
   }
}
