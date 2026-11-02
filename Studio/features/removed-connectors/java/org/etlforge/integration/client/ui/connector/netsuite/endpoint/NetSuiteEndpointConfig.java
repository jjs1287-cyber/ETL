package org.etlforge.integration.client.ui.connector.netsuite.endpoint;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.connector.TestConnectionAction;
import org.etlforge.integration.client.ui.connector.netsuite.endpoint.page.NetSuiteEndpointPage;
import org.etlforge.integration.client.ui.connector.netsuite.endpoint.page.NetSuiteEndpointPageConfig;
import org.etlforge.integration.client.ui.connector.netsuite.function.RefreshNetSuiteFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public final class NetSuiteEndpointConfig extends AbstractEntityConfig {
   private TestConnectionAction testConnectionAction;
   private ValueCache<NetSuiteEndpoint> valueCache = ValueCache.EMPTY;
   private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;
   private final NetSuiteCustomFieldsQueryBuilderLookup customFieldsLookup;
   private final NetSuiteSearchObjectSavedSearchLookup savedSearchLookup;
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;
   private final ConnectorCallInfoProvider infoProvider;
   private final MappingStructureFactory mappingStructureFactory;
   private RefreshNetSuiteFunctionJobFactory refreshFunctionJobFactory;

   public NetSuiteEndpointConfig(
      ConnectorCallInfoProvider var1,
      MappingStructureFactory var2,
      NetSuiteDefinitionQueryBuilderLookup var3,
      NetSuiteCustomFieldsQueryBuilderLookup var4,
      NetSuiteSearchObjectSavedSearchLookup var5,
      NetSuiteDefinitionDesignLookup var6,
      NetSuiteCustomFieldsDesignLookup var7
   ) {
      super(EntityType.NetSuiteEndpoint);
      this.searchDefLookup = var3;
      this.customFieldsLookup = var4;
      this.savedSearchLookup = var5;
      this.infoProvider = var1;
      this.defLookup = var6;
      this.cfLookup = var7;
      this.mappingStructureFactory = var2;
      this.refreshFunctionJobFactory = null;
      this.setSearchAndReplaceMetaData(new NetSuiteEndpointSearchAndReplaceProperties());
      ArrayList var8 = Lists.newArrayList();
      var8.add(new NetSuiteVersionChangeSaveConseqenceFactory(this));
      this.setSaveConsequenceFactories(var8);
   }

   public void setTestConnectionAction(TestConnectionAction var1) {
      this.testConnectionAction = var1;
   }

   public void setValueCache(ValueCache<NetSuiteEndpoint> var1) {
      this.valueCache = var1;
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      NetSuiteVersionChangeSaveConseqenceFactory var4 = new NetSuiteVersionChangeSaveConseqenceFactory(this);
      return new NetSuiteEndpointPageConfig(var1, var3, this.testConnectionAction, var4);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new NetSuiteEndpointPage((NetSuiteEndpoint)var1, this.valueCache);
   }

   @Override
   public void updateValueCache(IntegrationProject var1) {
      this.valueCache.update(var1.getAllEntitiesOfClass(NetSuiteEndpoint.class));
   }

   public RefreshNetSuiteFunctionJobFactory getRefreshJobFactory() {
      if (this.refreshFunctionJobFactory == null) {
         this.refreshFunctionJobFactory = this.generateRefreshJobFactory();
      }

      return this.refreshFunctionJobFactory;
   }

   private RefreshNetSuiteFunctionJobFactory generateRefreshJobFactory() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.getProject());
      FileStore var2 = var1.getFileStore();
      MappingStructureRefresher var3 = new MappingStructureRefresher(this.mappingStructureFactory);
      var3.setCache(var1.getTransformationMappingStructureCache());
      return new RefreshNetSuiteFunctionJobFactory(
         var2,
         this.getFileUploadApi(),
         var3,
         this.searchDefLookup,
         this.customFieldsLookup,
         this.savedSearchLookup,
         this.defLookup,
         this.cfLookup,
         this.infoProvider
      );
   }

   private final FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
