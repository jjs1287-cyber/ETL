package org.etlforge.integration.client.ui.connector.mscrm.function.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectCache;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class MSCrmFunctionWizardFactory extends EntityWizardFactory {
   private final MSCrmFunctionWizardInfoProvider infoProvider;
   private final MSCrmObjectCache objectCache;
   private final EntityExplorerSupport projectExplorer;

   public MSCrmFunctionWizardFactory(MSCrmFunctionWizardInfoProvider var1, MSCrmObjectCache var2, EntityExplorerSupport var3) {
      this.infoProvider = var1;
      this.objectCache = var2;
      this.projectExplorer = var3;
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      this.cleanUpCache();
      MSCrmFunction var4 = (MSCrmFunction)var2;
      MSCrmFunctionWizard var5 = new MSCrmFunctionWizard(var4, var3, this.infoProvider, var1.getProjectExplorer(), var1.getFileStore());
      var5.addWizardListener(new EntityWizardCompleter<>(var5, var1.getProject(), var1.getProjectPersistor(), var3));
      this.createWizardDisplayer(var5, var1, var4.getEntityType());
      this.installPageChain(var5, var4, var3);
      return var5;
   }

   private void cleanUpCache() {
      Thread var1 = new Thread(new Runnable() {
         @Override
         public void run() {
            MSCrmFunctionWizardFactory.this.objectCache.evictOldEntries();
         }
      });
      var1.setPriority(3);
      var1.start();
   }

   private void installPageChain(MSCrmFunctionWizard var1, MSCrmFunction var2, Folder var3) {
      ObjectSelectionModel var4 = ObjectSelectionModelFactory.newModel();
      ObjectSelectionController var5 = new ObjectSelectionController(var4, this.infoProvider);
      var5.setCache(this.objectCache.getBasicDetailsCache());
      var5.setWaitMessage("Downloading list of objects");
      MSCrmFunctionWizardPageChain var6 = new MSCrmFunctionWizardPageChain(var1, var4, var5, this.infoProvider.getObjectLookupFactory(), this.projectExplorer);
      var1.setPageChain(var6);
   }

   @Override
   protected String[] getSections() {
      return MSCrmFunctionWizardSections.all();
   }
}
