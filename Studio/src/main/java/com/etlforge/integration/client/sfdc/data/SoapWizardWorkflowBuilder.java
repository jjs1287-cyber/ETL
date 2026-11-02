package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.entity.operation.OperationUtils;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class SoapWizardWorkflowBuilder<T extends IntegrationEntity & SalesforceWizardEntity> {
   private final IntegrationProject project;
   private final T wizardItem;
   private final Folder wizardParent;
   private final SfdcMethod sfdcMethod;
   private final WizardFolders folders;
   private final Operation operation;
   private final OperationPipeline pipeline;
   private final WebServiceCall wsCall;
   private final Transformation request;
   private volatile Transformation response;
   private volatile SalesforceConnector connector;
   private volatile SObjectSchema sobject;
   private volatile ImplementingObject<TextDocument> responseDocument;

   public SoapWizardWorkflowBuilder(IntegrationProject var1, T var2, SfdcMethod var3, Folder var4) {
      this(var1, (T)var2, var3, var4, null);
   }

   public SoapWizardWorkflowBuilder(IntegrationProject var1, T var2, SfdcMethod var3, Folder var4, Transformation var5) {
      this.project = var1;
      this.wizardItem = (T)var2;
      this.wizardParent = var4;
      this.sfdcMethod = var3;
      this.folders = new WizardFolders(var1, var2.getEntityType(), var4);
      this.wsCall = this.createWebServiceCall();
      this.request = var5 == null ? this.createRequest() : var5;
      this.response = this.createResponse();
      this.pipeline = this.startPipeline();
      this.operation = this.createOperation();
   }

   private WebServiceCall createWebServiceCall() {
      WebServiceCallProvider var1 = new WebServiceCallProvider(this.project, this.sfdcMethod);
      return var1.getWebServiceCall();
   }

   private Transformation createRequest() {
      Transformation var1 = new Transformation("Request");
      var1.setTransient(true);
      var1.setPartOfSalesforceWizard(true);
      var1.setSalesforceWizardRole(SalesforceWizardRole.request(this.sfdcMethod));
      var1.setInputStructure(new NoStructure());
      var1.setOutputStructure(this.wsCall.getInputStructure());
      return var1;
   }

   private Transformation createResponse() {
      Transformation var1 = new Transformation("Response");
      var1.setTransient(true);
      var1.setPartOfSalesforceWizard(true);
      var1.setSalesforceWizardRole(SalesforceWizardRole.response(this.sfdcMethod));
      var1.setInputStructure(this.wsCall.getOutputStructure());
      var1.setOutputStructure(new NoStructure());
      return var1;
   }

   private OperationPipeline startPipeline() {
      OperationPipeline var1 = WebServiceOperationType.TYPE.createNewTemplate();
      WebServicePipelineBrowser var2 = new WebServicePipelineBrowser(var1);
      var2.setRequest(this.request);
      var2.getRequestActivity().setReadOnly(true);
      var2.setFunction(this.wsCall);
      var2.getFunctionActivity().setReadOnly(true);
      var2.setResponse(this.response);
      var2.getResponseActivity().setReadOnly(true);
      return var1;
   }

   private Operation createOperation() {
      Operation var1 = new Operation("Operation");
      var1.setOperationType(WebServiceOperationType.TYPE);
      var1.setConnectType(this.wizardItem.getEntityType());
      var1.setSalesforceWizardGuid(this.wizardItem);
      var1.setSalesforceWizardMethodName(this.sfdcMethod.getMethodName());
      var1.setTransient(true);
      var1.setPartOfSalesforceWizard(true);
      this.wizardItem.setImplementingOperation(var1);
      return var1;
   }

   public void setOperationPreferences(OperationPreferences var1) {
      var1.apply(this.operation);
   }

   public void setConnector(SalesforceConnector var1) {
      this.connector = Preconditions.checkNotNull(var1);
   }

   public SalesforceConnector getConnector() {
      return this.connector;
   }

   public void setRequestReadOnly(boolean var1) {
      WebServicePipelineBrowser var2 = new WebServicePipelineBrowser(this.pipeline);
      var2.getRequestActivity().setReadOnly(var1);
   }

   public void setResponseReadOnly(boolean var1) {
      WebServicePipelineBrowser var2 = new WebServicePipelineBrowser(this.pipeline);
      var2.getResponseActivity().setReadOnly(var1);
   }

   public void setSalesforceObject(SObjectSchema var1) {
      this.sobject = var1;
      this.associateTransformationWithSalesforceObject(this.request, var1);
      this.associateTransformationWithSalesforceObject(this.response, var1);
   }

   private void associateTransformationWithSalesforceObject(Transformation var1, SObjectSchema var2) {
      if (var1.isEditable()) {
         var1.setSalesforceObject(var2.getExpandedName());
      }
   }

   public String getObjectName() {
      return this.sobject.getName();
   }

   public void setWizardName(String var1) {
      this.operation.setName(var1);
      if (this.request.isEditable()) {
         this.request.setName(var1 + " Request");
      }

      if (this.response.isEditable()) {
         this.response.setName(var1 + " Response");
      }
   }

   public void setSource(Source var1) {
      OperationPipelineBrowser var2 = new OperationPipelineBrowser(this.pipeline);
      var2.setSource(var1);
   }

   public void removeSourceActivity() {
      SourceActivity var1 = this.pipeline.getFirstActivityOfType(SourceActivity.class);
      if (var1 != null) {
         this.pipeline.removeActivity(var1);
      }
   }

   public void setTarget(Target var1) {
      OperationPipelineBrowser var2 = new OperationPipelineBrowser(this.pipeline);
      var2.setTarget(var1);
   }

   public void removeTargetActivity() {
      TargetActivity var1 = this.pipeline.getFirstActivityOfType(TargetActivity.class);
      if (var1 != null) {
         this.pipeline.removeActivity(var1);
      }
   }

   public void removeTargetActivityIfNotNeeded() {
      if (this.response == SystemTransformation.SALESFORCE_STATUS_RESPONSE) {
         this.removeTargetActivity();
      }
   }

   public void startOver() {
      this.setSource(null);
      this.setTarget(null);
      this.request.setInputStructure(new NoStructure());
      this.response.setOutputStructure(new NoStructure());
      this.folders.startOver();
   }

   public SfdcMethod getMethod() {
      return this.sfdcMethod;
   }

   public T getWizardItem() {
      return this.wizardItem;
   }

   public Operation getOperation() {
      return this.operation;
   }

   public Transformation getRequest() {
      return this.request;
   }

   public Transformation getResponse() {
      return this.response;
   }

   public void useBuiltInModifierResponse() {
      this.setResponse(SystemTransformation.SALESFORCE_STATUS_RESPONSE);
      this.responseDocument = null;
   }

   private void setResponse(Transformation var1) {
      this.response = Preconditions.checkNotNull(var1);
      WebServicePipelineBrowser var2 = new WebServicePipelineBrowser(this.pipeline);
      var2.setResponse(var1);
   }

   public boolean isUsingBuiltInModifierResponse() {
      return this.response == SystemTransformation.SALESFORCE_STATUS_RESPONSE;
   }

   public void useCustomModifierResponse(SalesforceModifierResponseMapper var1) {
      if (this.response == SystemTransformation.SALESFORCE_STATUS_RESPONSE) {
         this.recreateResponse();
      }

      SalesforceModifierResponse var2 = new SalesforceModifierResponse(this.project, this.sfdcMethod, this.folders, var1);
      var2.applyTo(this.response);
      this.responseDocument = var2.getResponseDocument();
   }

   private void recreateResponse() {
      this.response = this.createResponse();
      this.setResponse(this.response);
   }

   public SoapWizardWorkflow<T> buildWorkflow(List<ImplementingObject<?>> var1) {
      ImplementingObject var2 = ImplementingObject.newObject(this.wizardParent, this.wizardItem);
      Folder var3 = this.folders.getWizardSpecificFolder(EntityType.Operation);
      this.operation.setPipeline(this.pipeline);
      OperationUtils.applyDeployProperties(this.operation, this.pipeline);
      ImplementingObject var4 = this.associateWithFolder(this.operation, var3);
      Folder var5 = this.folders.getWizardSpecificFolder(EntityType.Transformation);
      ImplementingObject var6 = this.associateWithFolder(this.request, var5);
      ImplementingObject var7 = this.associateWithFolder(this.response, var5);
      ImplementingObject var8 = this.associateWsCallWithFolder();
      ImplementingObject var9 = this.associateConnectorWithFolder();
      ImplementingObject var10 = this.associateSourceWithFolder();
      ImplementingObject var11 = this.associateTargetWithFolder();
      ArrayList var12 = Lists.newArrayList(var8, var9, var10, var11);
      var12.addAll(this.folders.getFoldersCreatedByWizard());
      if (this.responseDocument != null && this.responseDocument.isCreatedByWizard()) {
         var12.add(this.responseDocument);
      }

      var12.addAll(var1);
      return new SoapWizardWorkflow<>(this.project, var2, var4, var6, var7, KongaListUtils.filter(var12, Predicates.notNull()));
   }

   private <E extends IntegrationEntity> ImplementingObject<E> associateWithFolder(E var1, Folder var2) {
      if (var1 instanceof BuiltInIntegrationEntity) {
         return ImplementingObject.forExistingObject((E)var1);
      }

      ImplementingObject var3 = ImplementingObject.newObject(var2, var1);
      var3.suggestNewName(var1.getName());
      return var3;
   }

   private ImplementingObject<WebServiceCall> associateWsCallWithFolder() {
      if (!this.wsCall.isTransient()) {
         return null;
      }

      Folder var1 = this.folders.getWizardTypeRootFolder(EntityType.WebServiceCall);
      return this.associateWithFolder(this.wsCall, var1);
   }

   private ImplementingObject<SalesforceConnector> associateConnectorWithFolder() {
      if (!this.connector.isTransient()) {
         return null;
      }

      Folder var1 = this.folders.getParentForNewProjectItem(this.connector);
      return this.associateWithFolder(this.connector, var1);
   }

   private ImplementingObject<Source> associateSourceWithFolder() {
      OperationPipelineBrowser var1 = new OperationPipelineBrowser(this.pipeline);
      Source var2 = var1.getSource();
      if (var2 != null && var2.isTransient()) {
         Folder var3 = this.folders.getParentForNewProjectItem(var2);
         return this.associateWithFolder(var2, var3);
      } else {
         return null;
      }
   }

   private ImplementingObject<Target> associateTargetWithFolder() {
      OperationPipelineBrowser var1 = new OperationPipelineBrowser(this.pipeline);
      Target var2 = var1.getTarget();
      if (var2 != null && var2.isTransient()) {
         Folder var3 = this.folders.getParentForNewProjectItem(var2);
         return this.associateWithFolder(var2, var3);
      } else {
         return null;
      }
   }

   public <E extends IntegrationEntity> ImplementingObject<E> associateItemWithWizardRootFolder(E var1) {
      if (var1 instanceof BuiltInIntegrationEntity) {
         return ImplementingObject.forExistingObject((E)var1);
      }

      Folder var2 = this.folders.getWizardSpecificFolder(var1.getEntityType());
      return this.associateWithFolder((E)var1, var2);
   }

   public void setSoapHeaders(SoapHeader... var1) {
      SoapHeaderSetter var2 = new SoapHeaderSetter(new SoapHeaderTranslator(), var1);
      var2.applyHeaders(this.wizardItem);
   }
}
