package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Preconditions;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import com.etlforge.integration.data.download.entity.DefaultEntityFactory;
import com.etlforge.integration.data.download.entity.EntityFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public abstract class SfdcItemDownloadConfig<T extends IntegrationEntity & SalesforceWizardEntity> implements ItemDownloadConfig {
   private final Class<T> wizardClass;
   private final EntityFactory factory;

   protected SfdcItemDownloadConfig(EntityType var1, Class<T> var2) {
      this.wizardClass = Preconditions.checkNotNull(var2, "wizardClass");
      this.factory = new DefaultEntityFactory(var1);
   }

   @Override
   public final EntityFactory getFactory() {
      return this.factory;
   }

   @Override
   public final void doPostProjectAssemblyProcessing(IntegrationProject var1) {
      for (IntegrationEntity var3 : var1.getAllEntitiesOfClass(this.wizardClass)) {
         this.processWizard((T)var3, var1);
      }
   }

   private void processWizard(T var1, IntegrationProject var2) {
      this.protectPipeline((T)var1, var2.getItemLookup());
      this.doAdditionalProcessing((T)var1, var2);
   }

   private void protectPipeline(T var1, IntegrationEntityLookup var2) {
      OperationPipelineProtector var3 = this.createPipelineProtector();
      var3.protectPipeline((SalesforceWizardEntity)var1, var2);
   }

   protected abstract OperationPipelineProtector createPipelineProtector();

   protected void doAdditionalProcessing(T var1, IntegrationProject var2) {
   }
}
