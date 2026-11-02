package org.etlforge.integration.client.ui.connector.sap.function.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class SapFunctionWizardFactory extends EntityWizardFactory {
   private final ConnectorCallInfoProvider infoProvider;
   private final ConnectorObjectCache objectCache;

   public SapFunctionWizardFactory(ConnectorCallInfoProvider var1, ConnectorObjectCache var2) {
      this.infoProvider = var1;
      this.objectCache = var2;
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      this.cleanUpCache();
      SapFunction var4 = (SapFunction)var2;
      SapFunctionWizard var5 = new SapFunctionWizard(var4, var3, this.infoProvider, var1.getProjectExplorer(), var1.getFileStore());
      var5.addWizardListener(new EntityWizardCompleter<>(var5, var1.getProject(), var1.getProjectPersistor(), var3));
      this.createWizardDisplayer(var5, var1, EntityType.SapFunction);
      this.installPageChain(var5, var4, var3);
      return var5;
   }

   private void cleanUpCache() {
      Thread var1 = new Thread(new Runnable() {
         @Override
         public void run() {
            SapFunctionWizardFactory.this.objectCache.evictOldEntries();
         }
      });
      var1.setPriority(3);
      var1.start();
   }

   private void installPageChain(SapFunctionWizard var1, SapFunction var2, Folder var3) {
      SapFunctionWizardPageChain var4 = new SapFunctionWizardPageChain(var1, this.infoProvider, this.objectCache);
      var1.setPageChain(var4);
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all();
   }
}
