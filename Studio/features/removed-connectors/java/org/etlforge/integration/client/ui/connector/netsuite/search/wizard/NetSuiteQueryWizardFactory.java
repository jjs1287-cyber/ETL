package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connector.netsuite.serverapi.NetSuiteDesignInfoProvider;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.ui.connector.netsuite.NetSuiteObjectCache;
import org.etlforge.integration.client.ui.connector.netsuite.function.wizard.NetSuiteEntityWizardFactory;
import org.etlforge.integration.client.ui.connector.netsuite.function.wizard.NetSuiteObjectTypeFilter;
import org.etlforge.integration.client.ui.connector.netsuite.function.wizard.ObjectSelectionModelFactory;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectTypeFilter;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObjectCategory;

public final class NetSuiteQueryWizardFactory extends NetSuiteEntityWizardFactory {
   private final NetSuiteDesignInfoProvider infoProvider;
   private final NetSuiteObjectCache objectCache;
   private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;
   private final NetSuiteCustomFieldsQueryBuilderLookup customFieldsLookup;
   private final NetSuiteSearchObjectSavedSearchLookup savedSearchLookup;
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;
   private final TestNetSuiteQueryApi testQueryApi;
   private final EntityExplorerSupport projectExplorer;

   public NetSuiteQueryWizardFactory(
      NetSuiteDesignInfoProvider var1,
      NetSuiteObjectCache var2,
      NetSuiteDefinitionQueryBuilderLookup var3,
      NetSuiteCustomFieldsQueryBuilderLookup var4,
      NetSuiteSearchObjectSavedSearchLookup var5,
      NetSuiteDefinitionDesignLookup var6,
      NetSuiteCustomFieldsDesignLookup var7,
      TestNetSuiteQueryApi var8,
      EntityExplorerSupport var9
   ) {
      super(var6, var7);
      this.infoProvider = var1;
      this.objectCache = var2;
      this.searchDefLookup = var3;
      this.customFieldsLookup = var4;
      this.savedSearchLookup = var5;
      this.defLookup = var6;
      this.cfLookup = var7;
      this.testQueryApi = var8;
      this.projectExplorer = var9;
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      NetSuiteQuery var4 = (NetSuiteQuery)var2;
      NetSuiteQueryWizard var5 = new NetSuiteQueryWizard(var4, var3, this.infoProvider, var1.getProjectExplorer(), var1.getFileStore());
      EntityWizardCompleter var6 = new EntityWizardCompleter<>(var5, var1.getProject(), var1.getProjectPersistor(), var3);
      var5.addWizardListener(var6);
      this.createWizardDisplayer(var5, var1, EntityType.NetSuiteQuery);
      this.installPageChain(var5, var4, var3);
      return var5;
   }

   private void installPageChain(NetSuiteQueryWizard var1, NetSuiteQuery var2, Folder var3) {
      List var4 = getObjectTypeFilters();
      ObjectSelectionModel var5 = ObjectSelectionModelFactory.createObjectSelectionModel(var4);
      NetSuiteFunction.FunctionType var6 = var2.getFunctionType();
      var5.setSelectionPath(Collections.singleton(var6));
      ObjectSelectionController var7 = new ObjectSelectionController(var5, this.infoProvider);
      var7.setCache(this.objectCache.getObjectListCache());
      var7.setWaitMessage("Downloading a list of NetSuite Objects");
      NetSuiteQueryWizardPageChain var8 = new NetSuiteQueryWizardPageChain(
         var1,
         var5,
         var7,
         this.projectExplorer,
         this.searchDefLookup,
         this.customFieldsLookup,
         this.savedSearchLookup,
         this.defLookup,
         this.cfLookup,
         this.testQueryApi
      );
      var1.setPageChain(var8);
   }

   private static List<ObjectTypeFilter> getObjectTypeFilters() {
      ArrayList var0 = Lists.newArrayList();
      var0.add(NetSuiteObjectTypeFilter.of(NetSuiteConnectorObjectCategory.StandardObject));
      var0.add(NetSuiteObjectTypeFilter.of(NetSuiteConnectorObjectCategory.CustomObject));
      var0.add(NetSuiteObjectTypeFilter.of(NetSuiteConnectorObjectCategory.TranSearchObject));
      var0.add(NetSuiteObjectTypeFilter.of(NetSuiteConnectorObjectCategory.ItemSearchObject));
      return var0;
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all();
   }
}
