package org.etlforge.integration.client.ui.connector.netsuite.function.wizard;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.ui.connector.netsuite.NetSuiteObjectCache;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectTypeFilter;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObjectCategory;

public final class NetSuiteFunctionWizardFactory extends NetSuiteEntityWizardFactory {
   private final ConnectorCallInfoProvider infoProvider;
   private final NetSuiteObjectCache objectCache;
   private final EntityExplorerSupport projectExplorer;
   private NetSuiteFunction myFunction;

   public NetSuiteFunctionWizardFactory(
      ConnectorCallInfoProvider var1,
      NetSuiteObjectCache var2,
      EntityExplorerSupport var3,
      NetSuiteDefinitionDesignLookup var4,
      NetSuiteCustomFieldsDesignLookup var5
   ) {
      super(var4, var5);
      this.infoProvider = var1;
      this.objectCache = var2;
      this.projectExplorer = var3;
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      this.cleanUpCache();
      NetSuiteFunction var4 = (NetSuiteFunction)var2;
      this.myFunction = (NetSuiteFunction)var2;
      NetSuiteFunctionWizard var5 = new NetSuiteFunctionWizard(var4, var3, this.infoProvider, var1.getProjectExplorer(), var1.getFileStore());
      var5.addWizardListener(new EntityWizardCompleter<>(var5, var1.getProject(), var1.getProjectPersistor(), var3));
      this.createWizardDisplayer(var5, var1, var4.getEntityType());
      this.installPageChain(var5, var4, var3);
      return var5;
   }

   private void cleanUpCache() {
      Thread var1 = new Thread(new Runnable() {
         @Override
         public void run() {
            NetSuiteFunctionWizardFactory.this.objectCache.evictOldEntries();
         }
      });
      var1.setPriority(3);
      var1.start();
   }

   private void installPageChain(NetSuiteFunctionWizard var1, NetSuiteFunction var2, Folder var3) {
      List var4 = getObjectTypeFilters();
      ObjectSelectionModel var5 = ObjectSelectionModelFactory.createObjectSelectionModel(var4);
      NetSuiteFunction.FunctionType var6 = var2.getFunctionType();
      var5.setSelectionPath(Collections.singleton(var6));
      ObjectSelectionController var7 = new ObjectSelectionController(var5, this.infoProvider);
      var7.setCache(this.objectCache.getObjectListCache());
      var7.setWaitMessage("Downloading a list of NetSuite Objects");
      NetSuiteFunctionWizardPageChain var8 = new NetSuiteFunctionWizardPageChain(
         var1, var5, var7, this.projectExplorer, this.getNetSuiteDefinitionDesignLookup(), this.getNetSuiteCustomFieldsDesignLookup()
      );
      var1.setPageChain(var8);
   }

   private static List<ObjectTypeFilter> getObjectTypeFilters() {
      ArrayList var0 = Lists.newArrayList();
      var0.add(NetSuiteObjectTypeFilter.of(NetSuiteConnectorObjectCategory.StandardObject));
      var0.add(NetSuiteObjectTypeFilter.of(NetSuiteConnectorObjectCategory.CustomObject));
      return var0;
   }

   @Override
   protected String[] getSections() {
      return this.myFunction.getFunctionType() == NetSuiteFunction.FunctionType.DELETE
         ? NetSuiteFunctionWizardSections.endpointOnly()
         : NetSuiteFunctionWizardSections.all();
   }
}
