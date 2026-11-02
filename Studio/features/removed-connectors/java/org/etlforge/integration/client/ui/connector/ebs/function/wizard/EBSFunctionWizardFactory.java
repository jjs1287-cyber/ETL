package org.etlforge.integration.client.ui.connector.ebs.function.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EBSFunctionWizardFactory extends EntityWizardFactory {
   private final ConnectorCallInfoProvider infoProvider;
   private final ConnectorObjectCache objectCache;

   public EBSFunctionWizardFactory(ConnectorCallInfoProvider var1, ConnectorObjectCache var2) {
      this.infoProvider = var1;
      this.objectCache = var2;
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      this.cleanUpCache();
      EBSFunction var4 = (EBSFunction)var2;
      EBSFunctionWizard var5 = new EBSFunctionWizard(var4, var3, this.infoProvider, var1.getProjectExplorer(), var1.getFileStore());
      var5.addWizardListener(new EntityWizardCompleter<>(var5, var1.getProject(), var1.getProjectPersistor(), var3));
      this.createWizardDisplayer(var5, var1, EntityType.EBSFunction);
      this.installPageChain(var5, var4, var3);
      return var5;
   }

   private void cleanUpCache() {
      Thread var1 = new Thread(new Runnable() {
         @Override
         public void run() {
            EBSFunctionWizardFactory.this.objectCache.evictOldEntries();
         }
      });
      var1.setPriority(3);
      var1.start();
   }

   private void installPageChain(EBSFunctionWizard var1, EBSFunction var2, Folder var3) {
      EBSFunctionWizardPageChain var4 = new EBSFunctionWizardPageChain(var1, this.infoProvider, this.objectCache);
      var1.setPageChain(var4);
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all();
   }
}
