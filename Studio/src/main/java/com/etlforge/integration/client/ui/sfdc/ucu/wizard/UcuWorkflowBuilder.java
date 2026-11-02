package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.ImplementingObject;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflowBuilder;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSource;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizardConstants;
import com.etlforge.integration.client.ui.sfdc.wizard.WorkflowBuilder;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceUpsert;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.entity.operation.options.ChunkingOption;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.ArgCheck;

final class UcuWorkflowBuilder<T extends AbstractSalesforceUCU> implements WorkflowBuilder {
   private final SoapWizardWorkflowBuilder<T> base;
   private volatile RequestSource requestStructure;
   private String externalIdFieldName;

   public UcuWorkflowBuilder(IntegrationProject var1, T var2, Folder var3) {
      this.base = new SoapWizardWorkflowBuilder<>(var1, (T)var2, var2.getMethod(), var3);
      this.base.setResponseReadOnly(false);
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
   public void setOperationPreferences(OperationPreferences var1) {
      this.base.setOperationPreferences(var1);
   }

   @Override
   public void setConnector(SalesforceConnector var1) {
      this.base.setConnector(var1);
   }

   @Override
   public void setSalesforceObject(SObjectSchema var1) {
      this.base.setSalesforceObject(var1);
      this.base.getWizardItem().setSalesforceObjectName(var1.getExpandedName());
      this.setObjectName(var1.getLabelPlural());
   }

   private void setObjectName(String var1) {
      String var2 = this.base.getMethod().getDisplayName();
      this.base.setWizardName(var2 + " " + var1);
   }

   public void setExternalIdFieldName(String var1) {
      AbstractSalesforceUCU var2 = this.base.getWizardItem();
      Preconditions.checkState(var2 instanceof SalesforceUpsert, "This method is only applicable for SalesforceUpserts");
      ((SalesforceUpsert)var2).setExternalIdFieldName(var1);
      this.externalIdFieldName = ArgCheck.checkNotBlank(var1);
   }

   public void configureSource(Source var1, RequestSource var2) {
      this.requestStructure = var2;
      this.base.setSource(var1);
      AbstractSalesforceUCU var3 = this.base.getWizardItem();
      var2.applyTo(var3);
      Transformation var4 = this.base.getRequest();
      var2.applyTo(var4);
   }

   public void setRelatedObjects(List<RelatedObject> var1) {
      this.base.getWizardItem().setRelatedObjects(var1);
   }

   public SoapWizardWorkflow<T> buildWorkflow(Options var1) {
      this.configureOperation();
      this.mapRequestHeader(var1);
      this.configureResponse(var1);
      this.applyOptions(var1);
      this.base.removeTargetActivityIfNotNeeded();
      List var2 = this.getAdditionalObjects();
      return this.base.buildWorkflow(var2);
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

   private void mapRequestHeader(Options var1) {
      Transformation var2 = this.base.getRequest();
      SfdcMethod var3 = this.base.getMethod();
      SalesforceConnector var4 = this.base.getConnector();
      RequestHeaderMapper var5 = new RequestHeaderMapper(var2, var3, var4, RetryPolicy.getDefaultPolicy(), var1.getSoapHeaders());
      var5.setExternalIdFieldName(this.externalIdFieldName);
      var5.insertAllRequestMappings();
   }

   private void configureResponse(Options var1) {
      if (var1.useBuiltInResponse()) {
         this.base.useBuiltInModifierResponse();
      } else {
         ResponseMapper var2 = new ResponseMapper(this.base.getMethod(), this.base.getObjectName());
         var2.setExternalIdName(this.externalIdFieldName);
         this.base.useCustomModifierResponse(var2);
      }
   }

   private void applyOptions(Options var1) {
      this.base.setSoapHeaders(var1.getSoapHeaders());
      var1.applyTo(this.base.getWizardItem());
      var1.applyTo(this.base.getOperation());
   }

   private List<ImplementingObject<?>> getAdditionalObjects() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationEntity var3 : this.requestStructure.getItemsCreatedByWizard()) {
         ImplementingObject var4 = this.base.associateItemWithWizardRootFolder(var3);
         var1.add(var4);
      }

      return var1;
   }
}
