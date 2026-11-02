package org.etlforge.integration.client.ui.connector.msgp.function.wizard;

import java.util.Collections;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class MSGPFunctionWizardFactory extends EntityWizardFactory {
   private final ConnectorCallInfoProvider infoProvider;
   private final ConnectorObjectCache objectCache;
   private final EntityExplorerSupport projectExplorer;

   public MSGPFunctionWizardFactory(ConnectorCallInfoProvider var1, ConnectorObjectCache var2, EntityExplorerSupport var3) {
      this.infoProvider = var1;
      this.objectCache = var2;
      this.projectExplorer = var3;
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      this.cleanUpCache();
      MSGPFunction var4 = (MSGPFunction)var2;
      MSGPFunctionWizard var5 = new MSGPFunctionWizard(var4, var3, this.infoProvider, var1.getProjectExplorer(), var1.getFileStore());
      var5.addWizardListener(new EntityWizardCompleter<>(var5, var1.getProject(), var1.getProjectPersistor(), var3));
      this.createWizardDisplayer(var5, var1, var4.getEntityType());
      this.installPageChain(var5, var4, var3);
      return var5;
   }

   private void cleanUpCache() {
      Thread var1 = new Thread(new Runnable() {
         @Override
         public void run() {
            MSGPFunctionWizardFactory.this.objectCache.evictOldEntries();
         }
      });
      var1.setPriority(3);
      var1.start();
   }

   private void installPageChain(MSGPFunctionWizard var1, MSGPFunction var2, Folder var3) {
      ObjectSelectionModel var4 = new ObjectSelectionModel();
      MSGPFunction.FunctionType var5 = var2.getFunctionType();
      var4.setSelectionPath(Collections.singleton(var5));
      ObjectSelectionController var6 = new ObjectSelectionController(var4, this.infoProvider);
      var6.setCache(this.objectCache);
      var6.setWaitMessage("Downloading list of GP objects");
      MSGPFunctionWizardPageChain var7 = new MSGPFunctionWizardPageChain(var1, var4, var6, this.projectExplorer);
      var1.setPageChain(var7);
   }

   @Override
   protected String[] getSections() {
      return MSGPFunctionWizardSections.all();
   }
}
