package org.etlforge.integration.client.ui.connector.function;

import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.connector.ConnectorCallStructuresPanel;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.MappingStructureProvider;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public abstract class AbstractConnectorCallConfig<F extends AbstractConnectorCall<E, ?>, E extends ConnectorEndpoint> extends AbstractEntityConfig {
   private final EntityWizardFactory wizardFactory;
   private final TreeMapperInfoProvider treeMapperInfoProvider;
   private final MappingStructureFactory mappingStructureFactory;
   private final FunctionCallOperationType<? super F, ?> operationType;

   public AbstractConnectorCallConfig(
      EntityType var1, EntityWizardFactory var2, TreeMapperInfoProvider var3, MappingStructureFactory var4, FunctionCallOperationType<? super F, ?> var5
   ) {
      super(var1);
      this.wizardFactory = var2;
      this.treeMapperInfoProvider = var3;
      this.mappingStructureFactory = var4;
      this.operationType = var5;
   }

   @Override
   protected final IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      AbstractConnectorCall var2 = (AbstractConnectorCall)var1;
      TreeWidget var3 = this.createEndpointSelector();
      var3.setSelectedEntity(var2.getEndpoint());
      var3.setSelectorLabel("&Endpoint");
      ConnectorCallStructuresPanel var4 = new ConnectorCallStructuresPanel(var2);
      MappingStructureProvider var5 = this.createStructureProvider();
      var5.setWaitService(var4.asWaitService());
      return new ConnectorCallPage((F)var2, var3, var4, var5);
   }

   protected abstract TreeWidget<E> createEndpointSelector();

   private MappingStructureProvider createStructureProvider() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.getProject());
      InputAndOutputMappingTreeStructureCache var2 = var1.getConnectorCallMappingStructureCache();
      return new MappingStructureProvider(var2, this.treeMapperInfoProvider);
   }

   @Override
   public final EntityWizardFactory getEntityWizardFactory() {
      return this.wizardFactory;
   }

   @Override
   protected final PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      AbstractConnectorCall var4 = (AbstractConnectorCall)var2;
      RefreshFunctionJobFactory var5 = this.createRefreshJobFactory();
      List<? extends FunctionProperty> var6 = this.getProperties((F)var4, var5);
      return new ConnectorCallPageConfig<F, E>(var1, var3, this.getEntityFactory(), this.operationType, var5, var6);
   }

   private RefreshFunctionJobFactory createRefreshJobFactory() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.getProject());
      FileStore var2 = var1.getFileStore();
      MappingStructureRefresher var3 = new MappingStructureRefresher(this.mappingStructureFactory);
      var3.setCache(var1.getTransformationMappingStructureCache());
      return this.getRefreshFunctionJobFactory(var2, var3);
   }

   protected RefreshFunctionJobFactory getRefreshFunctionJobFactory(FileStore var1, MappingStructureRefresher var2) {
      return new RefreshFunctionJobFactory(var1, this.getFileUploadApi(), var2) {
         @Override
         public Callable<Schemas> getSchemaGenerator(ConnectorCall var1) {
            return AbstractConnectorCallConfig.this.getSchemaGenerator((F)var1);
         }
      };
   }

   protected abstract Callable<Schemas> getSchemaGenerator(F var1);

   protected abstract List<? extends FunctionProperty> getProperties(F var1, RefreshFunctionJobFactory var2);

   protected abstract FileUploadApi getFileUploadApi();
}
