package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.ImplementingObject;
import com.etlforge.integration.client.sfdc.data.WizardFolders;
import com.etlforge.integration.client.ui.sfdc.wizard.WorkflowBuilder;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.OperationUtils;
import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.sfdc.BulkLoadOperationType;
import org.etlforge.integration.data.entity.sfdc.BulkQueryOperationType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.bulkload.CreateJobParams;
import org.etlforge.integration.salesforce.bulkload.FileSplitterParams;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.ArgCheck;

final class BulkApiWorkflowBuilder implements WorkflowBuilder {
   private final IntegrationProject project;
   private final SalesforceBulkLoad wizardItem;
   private final Folder wizardParent;
   private final WizardFolders folders;
   private final Operation operation;
   private volatile SalesforceConnector connector;
   private volatile CreateJobParams createJobParams;
   private volatile BulkApiWorkflowBuilder.ActionTypeConfig actionTypeConfig;

   public BulkApiWorkflowBuilder(IntegrationProject var1, SalesforceBulkLoad var2, Folder var3) {
      this.project = var1;
      this.wizardItem = var2;
      this.wizardParent = var3;
      this.folders = new WizardFolders(var1, EntityType.SalesforceBulkLoad, var3);
      this.operation = this.createOperation();
   }

   private Operation createOperation() {
      Operation var1 = new Operation("Operation");
      var1.setTransient(true);
      var1.setConnectType(EntityType.SalesforceBulkLoad);
      var1.setSalesforceWizardGuid(this.wizardItem);
      var1.setPartOfSalesforceWizard(true);
      this.wizardItem.setImplementingOperation(var1);
      return var1;
   }

   public Operation getOperation() {
      return this.operation;
   }

   @Override
   public void setConnector(SalesforceConnector var1) {
      this.connector = Preconditions.checkNotNull(var1);
   }

   @Override
   public void setOperationPreferences(OperationPreferences var1) {
      var1.apply(this.operation);
   }

   @Override
   public void setSalesforceObject(SObjectSchema var1) {
   }

   public void setCreateJobParams(CreateJobParams var1) {
      this.createJobParams = Preconditions.checkNotNull(var1);
   }

   public void configureLoad(Source var1, FileSplitterParams var2, LoadOptions var3) {
      this.actionTypeConfig = new BulkApiWorkflowBuilder.Load(
         Preconditions.checkNotNull(var1), Preconditions.checkNotNull(var2), Preconditions.checkNotNull(var3)
      );
   }

   public void configureQuery(String var1, Target var2, QueryOptions var3) {
      this.actionTypeConfig = new BulkApiWorkflowBuilder.Query(ArgCheck.checkNotBlank(var1), Preconditions.checkNotNull(var2), Preconditions.checkNotNull(var3));
   }

   public BulkApiWorkflow buildWorkflow() {
      this.applyCreateJobParams();
      this.actionTypeConfig.applyTo(this.wizardItem, this.operation);
      ImplementingObject var1 = ImplementingObject.newObject(this.wizardParent, this.wizardItem);
      Folder var2 = this.folders.getWizardSpecificFolder(EntityType.Operation);
      ImplementingObject var3 = this.associateWithFolder(this.operation, var2);
      var3.suggestNewName(this.wizardItem.getName());
      List var4 = this.getOtherObjectsCreatedByWizard();
      return new BulkApiWorkflow(this.project, var1, var3, var4);
   }

   private void applyCreateJobParams() {
      this.wizardItem.setAction(this.createJobParams.action);
      this.wizardItem.setObjectName(this.createJobParams.objectName);
      this.wizardItem.setExternalIdName(this.createJobParams.externalIdField);
   }

   private <E extends IntegrationEntity> ImplementingObject<E> associateWithFolder(E var1, Folder var2) {
      if (var1 instanceof BuiltInIntegrationEntity) {
         return ImplementingObject.forExistingObject((E)var1);
      }

      ImplementingObject var3 = ImplementingObject.newObject(var2, var1);
      var3.suggestNewName(var1.getName());
      return var3;
   }

   private List<ImplementingObject<?>> getOtherObjectsCreatedByWizard() {
      ArrayList var1 = Lists.newArrayList();
      ImplementingObject var2 = this.associateConnectorWithFolder();
      if (var2 != null) {
         var1.add(var2);
      }

      ImplementingObject var3 = this.associateEndpointWithFolder();
      if (var3 != null) {
         var1.add(var3);
      }

      var1.addAll(this.folders.getFoldersCreatedByWizard());
      return var1;
   }

   private ImplementingObject<SalesforceConnector> associateConnectorWithFolder() {
      if (!this.connector.isTransient()) {
         return null;
      }

      Folder var1 = this.folders.getParentForNewProjectItem(this.connector);
      return this.associateWithFolder(this.connector, var1);
   }

   private ImplementingObject<IntegrationEntity> associateEndpointWithFolder() {
      IntegrationEntity var1 = this.actionTypeConfig.getEndpoint();
      if (!var1.isTransient()) {
         return null;
      }

      Folder var2 = this.folders.getParentForNewProjectItem(var1);
      return this.associateWithFolder(var1, var2);
   }

   private abstract static class ActionTypeConfig {
      private final OperationType opType;
      private final BulkOptions options;

      protected ActionTypeConfig(OperationType var1, BulkOptions var2) {
         this.opType = var1;
         this.options = var2;
      }

      public final void applyTo(SalesforceBulkLoad var1, Operation var2) {
         this.configureOperation(var1, var2);
         this.configureWizardItem(var1);
         this.applyOptions(var1, var2);
      }

      private void configureOperation(SalesforceBulkLoad var1, Operation var2) {
         var2.setOperationType(this.opType);
         OperationPipeline var3 = this.buildPipeline(var1);
         var2.setPipeline(var3);
         OperationUtils.applyDeployProperties(var2, var3);
      }

      private OperationPipeline buildPipeline(SalesforceBulkLoad var1) {
         OperationPipeline var2 = this.opType.createNewTemplate();
         BulkLoadActivity var3 = var2.getFirstActivityOfType(BulkLoadActivity.class);
         var3.setContent(var1);
         this.setEndpoint(new OperationPipelineBrowser(var2));
         return var2;
      }

      protected abstract void setEndpoint(OperationPipelineBrowser var1);

      protected abstract void configureWizardItem(SalesforceBulkLoad var1);

      private void applyOptions(SalesforceBulkLoad var1, Operation var2) {
         this.options.applyTo(var1);
         this.options.applyTo(var2);
      }

      public abstract IntegrationEntity getEndpoint();
   }

   private static class Load extends BulkApiWorkflowBuilder.ActionTypeConfig {
      private final Source source;
      private final FileSplitterParams fileSplitParams;

      public Load(Source var1, FileSplitterParams var2, LoadOptions var3) {
         super(BulkLoadOperationType.TYPE, var3);
         this.source = var1;
         this.fileSplitParams = var2;
      }

      @Override
      protected void setEndpoint(OperationPipelineBrowser var1) {
         var1.setSource(this.source);
      }

      @Override
      protected void configureWizardItem(SalesforceBulkLoad var1) {
         var1.setBatchParams(this.fileSplitParams.batchFileParams);
         var1.setHeaderMappings(this.fileSplitParams.mappings);
         var1.setDateConversionParams(this.fileSplitParams.dateConversionParams);
      }

      @Override
      public IntegrationEntity getEndpoint() {
         return this.source;
      }
   }

   private static class Query extends BulkApiWorkflowBuilder.ActionTypeConfig {
      private final String queryString;
      private final Target target;

      public Query(String var1, Target var2, QueryOptions var3) {
         super(BulkQueryOperationType.TYPE, var3);
         this.queryString = var1;
         this.target = var2;
      }

      @Override
      protected void setEndpoint(OperationPipelineBrowser var1) {
         var1.setTarget(this.target);
      }

      @Override
      protected void configureWizardItem(SalesforceBulkLoad var1) {
         var1.setQueryString(this.queryString);
      }

      @Override
      public IntegrationEntity getEndpoint() {
         return this.target;
      }
   }
}
