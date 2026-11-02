package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectCache;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.connector.MSCrmQuery;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class MSCrmQueryWizardFactory extends EntityWizardFactory {
   private final MSCrmDesignInfoProvider infoProvider;
   private final MSCrmObjectCache objectCache;

   public MSCrmQueryWizardFactory(MSCrmDesignInfoProvider var1, MSCrmObjectCache var2) {
      this.infoProvider = var1;
      this.objectCache = var2;
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all();
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      this.cleanUpCache();
      MSCrmQuery var4 = (MSCrmQuery)var2;
      MSCrmObjectLookupFactory var5 = new MSCrmObjectLookupFactory(this.objectCache, this.infoProvider);
      MSCrmQueryWizard var6 = new MSCrmQueryWizard(var4, var3, this.infoProvider, var5, var1.getProjectExplorer(), var1.getFileStore());
      var6.addWizardListener(new EntityWizardCompleter<>(var6, var1.getProject(), var1.getProjectPersistor(), var3));
      this.createWizardDisplayer(var6, var1, EntityType.MSCrmQuery);
      this.installPageChain(var6, var3);
      return var6;
   }

   private void cleanUpCache() {
      Thread var1 = new Thread(new Runnable() {
         @Override
         public void run() {
            MSCrmQueryWizardFactory.this.objectCache.evictOldEntries();
         }
      });
      var1.setPriority(3);
      var1.start();
   }

   private void installPageChain(MSCrmQueryWizard var1, Folder var2) {
      MSCrmQueryWizardPageChain var3 = new MSCrmQueryWizardPageChain(var1, this.infoProvider, this.objectCache);
      var1.setPageChain(var3);
   }
}
