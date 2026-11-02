package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.ImplementingObject;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflowBuilder;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSource;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizardConstants;
import com.etlforge.integration.client.ui.sfdc.wizard.WorkflowBuilder;
import com.etlforge.integration.data.entity.SalesforceDelete;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.entity.operation.options.ChunkingOption;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

final class DeleteWorkflowBuilder implements WorkflowBuilder {
   private final SoapWizardWorkflowBuilder<SalesforceDelete> base;
   private volatile RequestSource requestStructure;

   public DeleteWorkflowBuilder(IntegrationProject var1, SalesforceDelete var2, Folder var3) {
      this.base = new SoapWizardWorkflowBuilder<>(var1, var2, SfdcMethod.DELETE, var3);
      this.base.setResponseReadOnly(false);
      this.base.setWizardName(var2.getName());
   }

   public Operation getOperation() {
      return this.base.getOperation();
   }

   public Transformation getRequest() {
      return this.base.getRequest();
   }

   public Transformation getResponse() {
      return this.base.getResponse();
   }

   @Override
   public void setConnector(SalesforceConnector var1) {
      this.base.setConnector(var1);
   }

   @Override
   public void setOperationPreferences(OperationPreferences var1) {
      this.base.setOperationPreferences(var1);
   }

   @Override
   public void setSalesforceObject(SObjectSchema var1) {
   }

   public void configureSource(Source var1, RequestSource var2) {
      this.requestStructure = var2;
      this.base.setSource(var1);
      SalesforceDelete var3 = this.base.getWizardItem();
      var2.applyTo(var3);
      Transformation var4 = this.base.getRequest();
      var2.applyTo(var4);
   }

   public SoapWizardWorkflow<SalesforceDelete> buildWorkflow(Options var1) {
      this.configureOperation();
      this.createRequestMappings(var1);
      this.configureResponse(var1);
      this.applyOptions(var1);
      this.base.removeTargetActivityIfNotNeeded();
      List var2 = this.getAdditionalObjects();
      return this.base.buildWorkflow(var2);
   }

   private void createRequestMappings(Options var1) {
      Transformation var2 = this.base.getRequest();
      SalesforceConnector var3 = this.base.getConnector();
      DeleteRequestMapper var4 = new DeleteRequestMapper();
      var4.insertAllRequestMappings(var2, var3, RetryPolicy.getDefaultPolicy(), var1.getSoapHeaders());
   }

   private void configureOperation() {
      Operation var1 = this.base.getOperation();
      short var2 = 200;
      int var3 = SalesforceWizardConstants.THREADS;
      Object var4 = null;
      Object var5 = null;
      ChunkingOption var6 = new ChunkingOption(var2, 0, var3, (String)var4, (String)var5);
      OperationOptions var7 = var1.getOperationOptions();
      OperationOptions var8 = var7.withChunkingOption(var6);
      var1.setOperationOptions(var8);
   }

   private void configureResponse(Options var1) {
      if (var1.useBuiltInResponse()) {
         this.base.useBuiltInModifierResponse();
      } else {
         DeleteResponseMapper var2 = new DeleteResponseMapper();
         this.base.useCustomModifierResponse(var2);
      }
   }

   private void applyOptions(Options var1) {
      var1.applyTo(this.base.getOperation());
      var1.applyTo(this.base.getWizardItem());
      this.base.setSoapHeaders(var1.getSoapHeaders());
   }

   private List<ImplementingObject<?>> getAdditionalObjects() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationEntity var3 : this.requestStructure.getItemsCreatedByWizard()) {
         ImplementingObject var4 = this.base.associateItemWithWizardRootFolder(var3);
         var1.add(var4);
      }

      return var1;
   }

   public void suggestName(String var1) {
      this.base.setWizardName(var1);
   }
}
