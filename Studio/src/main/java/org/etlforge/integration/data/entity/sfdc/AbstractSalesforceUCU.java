package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceModifier;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.StartAtRowProperty;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsListEncoder;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsOwner;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;
import org.etlforge.lang.Objects;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public abstract class AbstractSalesforceUCU extends IntegrationEntity implements SalesforceModifier, RelatedObjectsOwner {
   public static final String OBJECT = "salesforce_object";
   public static final String OPERATION_ID = "operation_id";
   public static final String REQUEST_HEADER = "upsert_request_header";
   protected static final String TRUNCATE_FIELDS = "truncate_fields";
   protected static final String TRIGGER_EMAIL = "trigger_email";
   protected static final String DEBUG_MODE = "debug_mode";
   public static final String INSERT_NULLS = "insert_null_values";
   public static final String RELATED_OBJECTS = "related_objects";
   public static final String COMPRESS = "compress";
   public static final boolean DEFAULT_COMPRESSION_OPTION = true;
   private final SfdcMethod method;

   protected AbstractSalesforceUCU(EntityType var1, IntegrationEntityId var2, String var3, SfdcMethod var4) {
      super(var1, var2, var3);
      this.method = var4;
      this.setCompressionEnabled(true);
   }

   @Override
   public final SfdcMethod getMethod() {
      return this.method;
   }

   @Override
   public final List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      return WizardEntityLinkCollector.collectLinks(this, var1);
   }

   @Override
   public final OperationId getImplementingOperationId() {
      return this.getProjectItemId("operation_id", EntityType.Operation, OperationId.class);
   }

   @Override
   public final void setImplementingOperation(Operation var1) {
      Preconditions.checkArgument(var1.getOperationType() == WebServiceOperationType.TYPE, "op must implement a web service call");
      this.setStringProperty("operation_id", var1.getID().toString());
   }

   private <T extends IntegrationEntityId> T getProjectItemId(String var1, EntityType var2, Class<T> var3) {
      synchronized (this.getDataLock()) {
         String var5 = this.getProperty(var1, null);
         return (T)(var5 == null ? null : var3.cast(var2.getIntegrationId(var5)));
      }
   }

   @Override
   public final SalesforceConnectorId getConnectorId() {
      return SalesforceWizardEntityUtils.getConnectorId(this);
   }

   @Override
   public final void setConnector(SalesforceConnector var1) {
      SalesforceWizardEntityUtils.setConnector(this, var1);
   }

   public final void setSalesforceObjectName(ExpandedName var1) {
      this.setStringProperty("salesforce_object", var1.toString());
   }

   public final ExpandedName getSalesforceObjectName() {
      String var1;
      synchronized (this.getDataLock()) {
         var1 = this.getProperty("salesforce_object");
      }

      if (StringUtils.isEmpty(var1)) {
         return null;
      }

      XmlNameFactory var2 = XmlNameFactory.conforming();
      return var2.newExpandedName(var1);
   }

   @Override
   public final ExpandedName getObject() {
      return this.getSalesforceObjectName();
   }

   public final boolean isBasedOnPartnerWsdl() {
      ExpandedName var1 = this.getSalesforceObjectName();
      return var1 != null && SalesforceWsdlConstants.isPartnerWsdlObject(var1);
   }

   @Deprecated
   public final UCURequestHeader getRequestHeader() {
      synchronized (this.getDataLock()) {
         boolean var2 = this.getBooleanProperty("truncate_fields", true);
         boolean var3 = this.getBooleanProperty("trigger_email", false);
         SfdcDebugMode var4 = this.getEnumProperty("debug_mode", SfdcDebugMode.class, UCURequestHeader.DEBUG_MODE_DEFAULT);
         return new UCURequestHeader(var2, var3, var4);
      }
   }

   @Deprecated
   public final void setRequestHeader(UCURequestHeader var1) {
      UCURequestHeader var2;
      synchronized (this.getDataLock()) {
         var2 = this.getRequestHeader();
         StringKeyValuePairs var4 = this.getKeyValues();
         var4.putBoolean("truncate_fields", var1.isTruncateFieldsEnabled());
         var4.putBoolean("trigger_email", var1.isEmailTriggerEnabled());
         var4.putEnum("debug_mode", var1.getDebugMode());
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("upsert_request_header", var2, var1);
      }
   }

   @Override
   public final void replaceReferences(ReferenceReplacementService var1) {
      SalesforceWizardReferencesReplacer var2 = new SalesforceWizardReferencesReplacer(var1);
      var2.replaceReferences(this);
   }

   @Override
   public final boolean isCopyable() {
      return false;
   }

   @Override
   public final boolean isDeepCopyable() {
      return true;
   }

   @Override
   public final Set<SalesforceWizardEntity.Header> getSoapHeaders() {
      return SoapHeaders.read(this);
   }

   @Override
   public final void setSoapHeaders(Set<SalesforceWizardEntity.Header> var1) {
      SoapHeaders.store(this, var1);
   }

   public final boolean getInsertNullValues() {
      return this.getBooleanProperty("insert_null_values", false);
   }

   public final void setInsertNullValues(boolean var1) {
      this.setBooleanProperty("insert_null_values", var1);
   }

   public final boolean isCompressionEnabled() {
      return this.getBooleanProperty("compress", true);
   }

   public final void setCompressionEnabled(boolean var1) {
      this.setBooleanProperty("compress", var1, true);
   }

   public abstract boolean hasExternalIdField();

   @Override
   protected final void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      if (this.isBasedOnPartnerWsdl()) {
         SalesforceWizardEntityUtils.validateWizardHasConnector(this, var1);
      }
   }

   @Override
   public final void setSourceFileHasHeader(boolean var1) {
      this.setBooleanProperty("source_file_has_header", var1);
   }

   @Override
   public final boolean getSourceFileHasHeader() {
      synchronized (this.getDataLock()) {
         return this.getBooleanProperty("source_file_has_header", false);
      }
   }

   @Override
   public final void setStartAtRow(StartAtRowProperty var1) {
      this.setLongProperty("start_at_row", var1.getRow());
   }

   @Override
   public final StartAtRowProperty getStartAtRow() {
      synchronized (this.getDataLock()) {
         long var2 = StartAtRowProperty.firstRow();
         long var4 = Math.max(var2, this.getLongProperty("start_at_row", var2));
         return new StartAtRowProperty(var4);
      }
   }

   public final Optional<DateConversionParams> getDateConversionParams() {
      return DateConversionProperties.restore(this);
   }

   public final void setDateConversionParams(Optional<DateConversionParams> var1) {
      Optional var2 = DateConversionProperties.store(this, var1);
      if (!Objects.equal(var2, var1)) {
         this.firePropertyChange("date_conversion_params", var2, var1);
      }
   }

   @Override
   public final void setFileEncodingName(String var1) {
      this.setStringProperty("source_encoding", var1);
   }

   @Override
   public final String getFileEncodingName() {
      synchronized (this.getDataLock()) {
         return this.getProperty("source_encoding", "");
      }
   }

   @Override
   public final void setRelatedObjects(List<RelatedObject> var1) {
      String var2 = RelatedObjectsListEncoder.encode(var1);
      this.setStringProperty("related_objects", var2);
   }

   @Override
   public final List<RelatedObject> getRelatedObjects() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("related_objects");
         return RelatedObjectsListEncoder.decode(var2);
      }
   }

   @Override
   public final StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = super.getCopyOfKeyValues();
      applyDefaultValueForDeploy(var1, "trigger_email", "0");
      applyDefaultValueForDeploy(var1, "truncate_fields", "0");
      return var1;
   }

   private static void applyDefaultValueForDeploy(StringKeyValuePairs var0, String var1, String var2) {
      String var3 = var0.get(var1);
      if (Strings.isNullOrEmpty(var3)) {
         var0.put(var1, var2);
      }
   }
}
