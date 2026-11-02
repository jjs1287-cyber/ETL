package org.etlforge.integration.data.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.base.ListenerReport;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.OperationLinkCollector;
import org.etlforge.integration.data.entity.operation.OperationReferenceReplacer;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.OperationUtils;
import org.etlforge.integration.data.entity.operation.OperationValidator;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.flow.OperationFlowActivity;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctions;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineMemento;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServicePipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public final class Operation extends IntegrationEntity implements OperationFlowActivity {
   @IdProperty
   public static final String OPERATION_ID = "operation_id";
   @NameProperty
   public static final String OPERATION_NAME = "operation_name";
   public static final String OPERATION_TYPE_ID = "operation_type_id";
   public static final String SCHEDULE = "schedule_id";
   public static final String SUCCESS_MESSAGE = "success_message_id";
   public static final String SUCCESS_OPERATION = "success_operation_id";
   public static final String FAILURE_MESSAGE = "failure_message_id";
   public static final String FAILURE_OPERATION = "failure_operation_id";
   public static final String SOURCE_ID = "source_id";
   public static final String TARGET_ID = "target_id";
   public static final String TRANSFORMATION_ID = "transformation_id";
   public static final String TRACE = "trace";
   public static final String DEBUG_MODE = "debug_mode";
   public static final String USER_ID = "user_id";
   public static final String DESCRIPTION = "description";
   public static final String IGNORE_HISTORY = "ignore_history";
   public static final String HOSTED_WEBSERVICE_ID = "hosted_webservice_id";
   public static final String CHUNK_SIZE = "chunk_size";
   public static final String TARGET_CHUNK_SIZE = "target_chunk_size";
   public static final String MAX_NUMBER_OF_THREADS = "max_number_of_threads";
   public static final String SOURCE_CHUNK_NODE = "source_chunk_node";
   public static final String TARGET_CHUNK_NODE = "target_chunk_node";
   public static final String SCHEDULE_POLICY = "schedule_policy";
   public static final String SALESFORCE_WIZARD_METHOD_NAME = "salesforce_wizard_operation_name";
   public static final String SCHEDULE_ENABLED = "scheduleEnabled";
   public static final String FUNCTION_CHAIN = "Operation#FunctionChain";
   public static final String CONFIGURATION = "Operation#OperationConfiguration";
   public static final String SIBLINGS = "Operation#Siblings";
   public static final String PIPELINE = "Operation#Pipeline";
   public static final String CONNECT_TYPE = "connect_type";
   public static final String SALESFORCE_WIZARD_GUID = "salesforce_wizard_guid";
   public static final String TARGET_SUCCESS_RECORDS_TARGET = "target_records_target.success";
   public static final String TARGET_SUCCESS_RECORDS_EMAIL = "target_records_email.success";
   public static final String TARGET_FAILURE_RECORDS_TARGET = "target_records_target.failure";
   public static final String TARGET_FAILURE_RECORDS_EMAIL = "target_records_email.failure";
   public static final String VALIDATE_SOURCE_TEXT_FILE = "validate_source_text_file";
   public static final String VALID_SOURCE_RECORDS_TARGET = "valid_source_records.target";
   public static final String VALID_SOURCE_RECORDS_EMAIL = "valid_source_records.email";
   public static final String INVALID_SOURCE_RECORDS_TARGET = "invalid_source_records.target";
   public static final String INVALID_SOURCE_RECORDS_EMAIL = "invalid_source_records.email";
   private static final String[] PROPERTIES = new String[]{
      "operation_id",
      "operation_name",
      "operation_type_id",
      "source_id",
      "target_id",
      "schedule_id",
      "transformation_id",
      "success_operation_id",
      "success_message_id",
      "failure_operation_id",
      "failure_message_id",
      "user_id",
      "description",
      "trace",
      "ignore_history",
      "chunk_size",
      "target_chunk_size",
      "max_number_of_threads",
      "source_chunk_node",
      "target_chunk_node",
      "schedule_policy",
      "label",
      "debug_mode",
      "salesforce_wizard_object",
      "salesforce_wizard_operation_name",
      "connect_type",
      "salesforce_wizard_guid",
      "target_records_target.success",
      "target_records_email.success",
      "target_records_target.failure",
      "target_records_email.failure",
      "validate_source_text_file",
      "valid_source_records.target",
      "valid_source_records.email",
      "invalid_source_records.target",
      "invalid_source_records.email"
   };
   private List<IntegrationId> functionIds = Lists.newArrayList();
   private List<OperationId> siblings = Lists.newArrayList();
   private OperationPipeline pipeline;
   private OperationPipelineMemento pipelineMemento;
   private OperationType type;

   public Operation(String var1) {
      this(null, var1);
   }

   public Operation(OperationId var1, String var2) {
      super(EntityType.Operation, var1, var2);
   }

   public OperationId getID() {
      return (OperationId)super.getID();
   }

   public void setOperationType(OperationType var1) {
      synchronized (this.getDataLock()) {
         this.setIntProperty("operation_type_id", var1.getId());
         this.type = var1;
      }
   }

   public OperationType getOperationType() {
      synchronized (this.getDataLock()) {
         return this.type != null ? this.type : this.readOperationTypeFromKeyValues();
      }
   }

   private OperationType readOperationTypeFromKeyValues() {
      int var1 = this.getIntProperty("operation_type_id", -1);
      return var1 >= 0 ? OperationType.fromId(var1) : OperationType.getDefaultType();
   }

   public ScheduleId getScheduleId() {
      return EntityUtils.readId(ScheduleId.class, this, "schedule_id").orNull();
   }

   public void setScheduleId(ScheduleId var1) {
      EntityUtils.storeId(var1, ScheduleId.class, this, "schedule_id");
   }

   public void setSchedule(Schedule var1) {
      this.setScheduleId(var1 == null ? null : var1.getID());
   }

   public OperationOptions getOperationOptions() {
      synchronized (this.getDataLock()) {
         return new OperationOptions(this);
      }
   }

   public void setOperationOptions(OperationOptions var1) {
      OperationOptions var2;
      OperationOptions var3;
      synchronized (this.getDataLock()) {
         var2 = this.getOperationOptions();
         var1.applyTo(this);
         var3 = this.getOperationOptions();
      }

      if (!var2.equals(var3)) {
         this.firePropertyChange("Operation#OperationConfiguration", var2, var3);
      }
   }

   public boolean isScheduleEnabled() {
      return this.getBooleanProperty("scheduleEnabled", true);
   }

   public void setScheduleEnabled(boolean var1) {
      this.setBooleanProperty("scheduleEnabled", var1, true);
   }

   public TransformationId getTransformationId() {
      return EntityUtils.readId(TransformationId.class, this, "transformation_id").or(TransformationId.UNDEFINED_ID);
   }

   public SourceId getSourceId() {
      return EntityUtils.readId(SourceId.class, this, "source_id").orNull();
   }

   @VisibleForTesting
   public void setSource(Source var1) {
      this.setSourceId(var1 == null ? null : var1.getID());
   }

   @VisibleForTesting
   public void setSourceId(SourceId var1) {
      EntityUtils.storeId(var1, SourceId.class, this, "source_id");
   }

   public TargetId getTargetId() {
      return EntityUtils.readId(TargetId.class, this, "target_id").orNull();
   }

   @VisibleForTesting
   public void setTarget(Target var1) {
      this.setTargetId(var1 == null ? null : var1.getID());
   }

   @VisibleForTesting
   public void setTargetId(TargetId var1) {
      EntityUtils.storeId(var1, TargetId.class, this, "target_id");
   }

   public EmailMessageId getSuccessMessageId() {
      return EntityUtils.readId(EmailMessageId.class, this, "success_message_id").orNull();
   }

   public void setSuccessMessageId(EmailMessageId var1) {
      EntityUtils.storeId(var1, EmailMessageId.class, this, "success_message_id");
   }

   public void setSuccessMessage(EmailMessage var1) {
      this.setSuccessMessageId(var1 == null ? null : var1.getID());
   }

   public OperationId getSuccessOperationId() {
      return EntityUtils.readId(OperationId.class, this, "success_operation_id").orNull();
   }

   public void setSuccessOperationId(OperationId var1) {
      EntityUtils.storeId(var1, OperationId.class, this, "success_operation_id");
   }

   public void setSuccessOperation(Operation var1) {
      this.setSuccessOperationId(var1 == null ? null : var1.getID());
   }

   public EmailMessageId getFailureMessageId() {
      return EntityUtils.readId(EmailMessageId.class, this, "failure_message_id").orNull();
   }

   public void setFailureMessageId(EmailMessageId var1) {
      EntityUtils.storeId(var1, EmailMessageId.class, this, "failure_message_id");
   }

   public void setFailureMessage(EmailMessage var1) {
      this.setFailureMessageId(var1 == null ? null : var1.getID());
   }

   public OperationId getFailureOperationId() {
      return EntityUtils.readId(OperationId.class, this, "failure_operation_id").orNull();
   }

   public void setFailureOperationId(OperationId var1) {
      EntityUtils.storeId(var1, OperationId.class, this, "failure_operation_id");
   }

   public void setFailureOperation(Operation var1) {
      this.setFailureOperationId(var1 == null ? null : var1.getID());
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      OperationPipeline var2 = null;
      synchronized (this.getDataLock()) {
         if (this.isDeleted()) {
            var2 = this.pipelineMemento != null ? this.restorePipelineFromMemento(var1) : null;
         } else {
            if (this.pipeline == null) {
               if (this.pipelineMemento != null) {
                  this.pipeline = this.restorePipelineFromMemento(var1);
               } else {
                  this.pipeline = this.getOperationType().getPipelineFactory().createPipeline(this, var1);
               }

               if (this.pipeline.getType() == HostedWebServiceOperationType.TYPE) {
                  HostedWebServicePipelineBrowser var4 = new HostedWebServicePipelineBrowser(this.pipeline);
                  this.setHostedWebService(var4.getWebServiceCall());
               }
            }

            var2 = this.pipeline;
         }
      }

      OperationLinkCollector var3 = new OperationLinkCollector(this, var2, var1);
      return var3.collect();
   }

   public void functionWasDeleted(Function var1) {
      if (var1 != null) {
         int var2 = this.functionIds.indexOf(var1.getFunctionID());
         if (var2 != -1) {
            IntegrationEntityId var3 = var1 instanceof Transformation ? TransformationId.UNDEFINED_ID : WebServiceCallId.UNDEFINED_ID;
            this.functionIds.set(var2, var3);
            if (var2 == 0) {
               this.setProperty("transformation_id", var3);
            }

            this.firePropertyChange("Operation#FunctionChain", var1.getFunctionID(), null);
         }
      }
   }

   public List<IntegrationId> getFunctionIDs() {
      return Lists.newArrayList(this.functionIds);
   }

   public void setFunctionChain(OperationFunctions var1) {
      List var2;
      synchronized (this.getDataLock()) {
         var2 = this.functionIds;
         this.functionIds = var1.getFunctionIds();
         this.setProperty("transformation_id", var1.getFirstTransformationId());
      }

      this.firePropertyChange("Operation#FunctionChain", var2, Lists.newArrayList(this.functionIds));
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      OperationValidator var2 = new OperationValidator(this, var1);
      var2.validatePipeline();
      var2.validateChunkingParameter();
      var2.validateSource();
      var2.validateOperationChains();
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = super.getCopyOfKeyValues();
      var1.put("operation_id", this.getID().toString());
      var1.put("operation_name", this.getName());
      var1.put("operation_type_id", Integer.toString(this.getOperationType().getId()));
      var1.put("description", this.getDescription());
      OperationOptions var2 = this.getOperationOptions();
      var1.put("trace", "1");
      var1.put("ignore_history", var2.getSourceHistoryOption().isHistoryChecked() ? "0" : "1");
      return var1;
   }

   public Operation clone() {
      Operation var1 = (Operation)super.clone();
      var1.functionIds = Lists.newArrayList(this.functionIds);
      var1.siblings = Lists.newArrayList(this.siblings);
      var1.pipeline = null;
      return var1;
   }

   public void setHostedWebService(WebServiceCall var1) {
      EntityUtils.storeId(var1 == null ? null : var1.getID(), WebServiceCallId.class, this, "hosted_webservice_id");
   }

   public WebServiceCallId getHostedWebServiceId() {
      return EntityUtils.readId(WebServiceCallId.class, this, "hosted_webservice_id").orNull();
   }

   @Override
   public String[] getPropertyKeys() {
      return (String[])PROPERTIES.clone();
   }

   public List<OperationId> getSiblingOperations() {
      synchronized (this.getDataLock()) {
         return Lists.newArrayList(this.siblings);
      }
   }

   public void setSiblingOperations(Iterable<OperationId> var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var1 != null) {
         for (OperationId var4 : var1) {
            var2.add(var4);
         }
      }

      List var7;
      synchronized (this.getDataLock()) {
         var7 = this.siblings;
         this.siblings = var2;
      }

      this.firePropertyChange("Operation#Siblings", var7, Lists.newArrayList(var2));
   }

   public boolean canBeSibling(Operation var1, IntegrationEntityLookup var2) {
      if (var1 == this) {
         return false;
      }

      if (!this.isDeleted() && !var1.isDeleted()) {
         Transformation var3 = getFirstTransformation(var1, var2);
         if (var3 == null) {
            return false;
         }

         Transformation var4 = getFirstTransformation(this, var2);
         if (var4 != null) {
            DataStructure var7 = var4.getInputStructure();
            DataStructure var8 = var3.getInputStructure();
            return var7 != null && var7.equals(var8);
         }

         if (this.getOperationType() != HostedWebServiceOperationType.TYPE) {
            return false;
         }

         WebServiceCallId var5 = this.getHostedWebServiceId();
         DataStructure var6 = var3.getInputStructure();
         return var6 instanceof WebServiceRequestStructure && ((WebServiceRequestStructure)var6).getFunctionCallId().equals(var5);
      } else {
         return false;
      }
   }

   public int getChunkSize() {
      return this.getIntProperty("chunk_size", 0);
   }

   public int getTargetChunkSize() {
      return this.getIntProperty("target_chunk_size", 0);
   }

   public SchedulePolicy getSchedulePolicy() {
      int var1 = this.getIntProperty("schedule_policy", -1);
      return EnumWithIdLookup.lookupOr(var1, SchedulePolicy.getDefault());
   }

   public void setSchedulePolicy(SchedulePolicy var1) {
      SchedulePolicy var2 = this.getSchedulePolicy();
      this.setProperty("schedule_policy", var1.getId());
      if (var2 != var1) {
         this.firePropertyChange("schedule_policy", var2, var1);
      }
   }

   public OperationPipeline getPipeline() {
      synchronized (this.getDataLock()) {
         if (this.pipeline == null) {
            if (this.pipelineMemento != null) {
               this.pipeline = this.restorePipelineFromMemento(this.getEntityLookup());
            } else {
               this.pipeline = this.createPipeline();
            }
         }

         return this.pipeline;
      }
   }

   private IntegrationEntityLookup getEntityLookup() {
      IntegrationProject var1 = ProjectUtils.getProject(this);
      if (var1 == null) {
         throw new IllegalStateException("This operation does not belong to a project");
      } else {
         return var1.getItemLookup();
      }
   }

   private OperationPipeline createPipeline() {
      return this.getOperationType().getPipelineFactory().createPipeline(this, this.getEntityLookup());
   }

   public boolean hasPipeline() {
      synchronized (this.getDataLock()) {
         return this.pipeline != null;
      }
   }

   public void setPipeline(OperationPipeline var1) {
      synchronized (this.getDataLock()) {
         if (this.pipeline != null) {
            if (this.pipeline.equals(var1)) {
               return;
            }

            this.pipeline.replaceWith(var1);
         } else {
            this.pipeline = new OperationPipeline(var1);
         }

         this.pipelineMemento = new OperationPipelineMemento(this.pipeline);
         OperationUtils.applyDeployProperties(this, this.pipeline);
      }

      this.firePropertyChange("Operation#Pipeline", null, this.pipeline);
   }

   @Override
   protected void doAdditionalCleanupOnDelete() {
      if (this.pipeline != null) {
         this.pipelineMemento = new OperationPipelineMemento(this.pipeline);

         for (PipelineActivity var2 : this.pipeline.getActivities()) {
            var2.dispose();
         }

         this.pipeline = null;
      } else {
         this.pipelineMemento = null;
      }
   }

   public boolean isSchedulable() {
      synchronized (this.getDataLock()) {
         return this.getOperationType().isStartedByEngine() && this.isSourceSchedulable();
      }
   }

   private boolean isSourceSchedulable() {
      SourceId var1 = this.getSourceId();
      return var1 == null || Source.isSchedulable(var1);
   }

   public boolean isPartOfSalesforceWizard() {
      return SalesforceUtils.isSalesforceWizardObject(this) || this.getConnectType() == EntityType.SalesforceBulkLoad;
   }

   public void setPartOfSalesforceWizard(boolean var1) {
      SalesforceUtils.setSalesforceWizardObject(this, var1);
   }

   public void setSalesforceWizardMethodName(String var1) {
      this.setStringProperty("salesforce_wizard_operation_name", var1);
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      if (!this.isDeleted()) {
         this.writePipeline(var1);
      }

      this.writeSiblingIds(var1);
      this.writeFunctionIds(var1);
   }

   private void writePipeline(Persistor var1) {
      if (this.pipelineMemento == null) {
         if (this.pipeline == null) {
            return;
         }

         this.pipelineMemento = new OperationPipelineMemento(this.pipeline);
      }

      this.pipelineMemento.writeTo(var1.createChild("Pipeline"));
   }

   private void writeSiblingIds(Persistor var1) {
      Persistor var2 = var1.createChild("Siblings");

      for (OperationId var4 : this.siblings) {
         var2.createChild("Sibling").putString("opId", var4.toString());
      }
   }

   private void writeFunctionIds(Persistor var1) {
      Persistor var2 = var1.createChild("Functions");

      for (IntegrationId var4 : this.functionIds) {
         EntityType var5 = ((IntegrationEntityId)var4).getEntityType();
         Persistor var6 = var2.createChild("Fcn");
         var6.putString("type", var5.name());
         var6.putString("value", var4.toString());
      }
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      this.type = this.readOperationTypeFromKeyValues();
      this.restoreSiblingIds(var1);
      this.restoreFunctionIds(var1);
   }

   private void restoreSiblingIds(Persistor var1) {
      this.siblings = Lists.newArrayList();

      for (Persistor var3 : var1.getFirstChild("Siblings").getChildren("Sibling")) {
         this.siblings.add(new OperationId(var3.getString("opId")));
      }
   }

   private void restoreFunctionIds(Persistor var1) {
      this.functionIds = Lists.newArrayList();

      for (Persistor var3 : var1.getFirstChild("Functions").getChildren("Fcn")) {
         EntityType var4 = EntityType.valueOf(var3.getString("type"));
         this.functionIds.add(var4.getIntegrationId(var3.getString("value")));
      }
   }

   private static Transformation getFirstTransformation(Operation var0, IntegrationEntityLookup var1) {
      IntegrationId var2 = getFirstFunctionId(var0);
      return var2 instanceof TransformationId ? var1.getEntity((TransformationId)var2, Transformation.class) : null;
   }

   @Override
   public void restoreDependentData(Persistor var1, IntegrationEntityLookup var2) {
      synchronized (this.getDataLock()) {
         this.pipelineMemento = new OperationPipelineMemento(this.type, var1.getFirstChild("Pipeline"));
         this.pipeline = this.restorePipelineFromMemento(var2);
      }
   }

   private OperationPipeline restorePipelineFromMemento(IntegrationEntityLookup var1) {
      OperationPipeline var2 = this.pipelineMemento.restorePipeline(var1);
      if (this.isSubjectForJBCLIENT514()) {
         applyJBCLIENT514(var2);
      }

      return var2;
   }

   private boolean isSubjectForJBCLIENT514() {
      return this.isPartOfSalesforceWizard() && this.getOperationType() == WebServiceOperationType.TYPE;
   }

   private static void applyJBCLIENT514(OperationPipeline var0) {
      WebServicePipelineBrowser var1 = new WebServicePipelineBrowser(var0);
      TransformationActivity var2 = var1.getResponseActivity();
      if (var2 != null) {
         var2.setReadOnly(false);
      }
   }

   @Override
   public ListenerReport reportListeners() {
      ListenerReport var1 = super.reportListeners();
      if (this.pipeline != null) {
         this.pipeline.reportListeners(var1);
      }

      return var1;
   }

   private static IntegrationId getFirstFunctionId(Operation var0) {
      return var0.functionIds.isEmpty() ? null : var0.functionIds.get(0);
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      Operation var2 = (Operation)var1;
      this.functionIds = Lists.newArrayList(var2.functionIds);
      this.siblings = Lists.newArrayList(var2.siblings);
      this.pipeline = null;
      this.pipelineMemento = var2.pipelineMemento;
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      synchronized (this.getDataLock()) {
         OperationReferenceReplacer var3 = new OperationReferenceReplacer(this, var1);
         var3.replaceReferences();
      }
   }

   public void setConnectType(EntityType var1) {
      assert var1.isWizardType();
      this.setIntProperty("connect_type", var1.getId());
   }

   public EntityType getConnectType() {
      try {
         int var1 = this.getIntProperty("connect_type", -1);
         if (var1 != -1) {
            return EntityType.fromId(var1);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return null;
   }

   public boolean getsSourceFromPreviousOperation() {
      return SystemSource.SOURCE_FROM_PREVIOUS_OPERATION.getID().equals(this.getSourceId());
   }

   public boolean getsSourceFromHttpEndpoint() {
      return SystemSource.FROM_HTTP_ENDPOINT.getID().equals(this.getSourceId());
   }

   @Override
   public boolean isCopyable() {
      return this.getOperationType().isSelectable();
   }

   @Override
   public boolean isDeepCopyable() {
      return this.getOperationType().isSelectable();
   }

   @Override
   public Version getRequiredVersion() {
      Version var1 = super.getRequiredVersion();
      Version var2 = this.getOperationType().getVersionIntroduced();
      return var2.isLaterThan(var1) ? var2 : var1;
   }

   public IntegrationEntityId getSalesforceWizardGuid() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("salesforce_wizard_guid");
         if (StringUtils.isNotEmpty(var2)) {
            EntityType var3 = this.getConnectType();
            if (var3 != null) {
               return var3.getIntegrationId(var2);
            }
         }

         return null;
      }
   }

   public void setSalesforceWizardGuid(SalesforceWizardEntity var1) {
      this.setStringProperty("salesforce_wizard_guid", var1 == null ? null : var1.getID().toString());
   }

   public boolean isImplementingSalesforcePartnerWizard() {
      return this.getSalesforceWizardGuid() != null;
   }

   public boolean isImplementingSalesforcePartnerWizard(EnumSet<SfdcMethod> var1) {
      if (this.isImplementingSalesforcePartnerWizard()) {
         String var2 = this.getProperty("salesforce_wizard_operation_name");

         for (SfdcMethod var4 : var1) {
            if (var4.getMethodName().equals(var2)) {
               return true;
            }
         }
      }

      return false;
   }
}
